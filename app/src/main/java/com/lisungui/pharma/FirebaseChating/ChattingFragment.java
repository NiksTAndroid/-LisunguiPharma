package com.lisungui.pharma.FirebaseChating;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lisungui.pharma.R;
import com.lisungui.pharma.models.ChatNotificationSendResponce;
import com.lisungui.pharma.models.UserFirebaseModel;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.PrefManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.lisungui.pharma.utility.CommonUtility.updateUser;


public class ChattingFragment extends Fragment {

    private static final int PICK_PDF_CODE = 11;
    private static final int PLACE_PICKER_REQUEST = 12;

    private static final int PICK_IMAGE_REQUEST = 13;
    private static final int PICK_VIDEO_REQUEST = 14;

    private RecyclerView recyclerview_chat;
    EditText editTextgetmsg;
    ImageButton IB_attacment, IB_send;
    ProgressBar mProgressBar;
    LinearLayoutManager mLayoutManager;
    Context mContext;
    DatabaseReference database, databaseRecent;
    StorageReference videoDataBase;
    StorageReference imageDataBase;
    StorageReference documentDataBase;
    String msgReceiverID, currentUserID, getMsgReceiverName;
    String finalNodeId;
    private ArrayList<String> list_blocked_usersid = new ArrayList<>();
    List<ChattingMassageModel> chattingList = new ArrayList<>();
    NewChattingAdapter chattingAdapter;
    private File actualImage;
    private Uri mImageUri;
    private Uri selectedvideo;
    //    private SetInboxNotificationRequest setInboxNotificationRequest;
    RecentMassageModel recentModel;
    private ValueEventListener VEL_message, recentmassageValueEventListener;
    public static boolean active = false;
    private TextView tv_status;
    private DatabaseReference databaseReceiverUser;
    private ValueEventListener userDataValueEvent;
    private UserFirebaseModel receiverUserFirebaseModel;
    private String messageReceiverType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            msgReceiverID = bundle.getString("PHARMACYID");
            getMsgReceiverName = bundle.getString("PHARMACYNAME");
            messageReceiverType = bundle.getString("TYPE");
            getActivity().setTitle(getMsgReceiverName);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_chat_fragment_, container, false);
        mContext = getActivity();
        recyclerview_chat = rootView.findViewById(R.id.recycler_view_chat);
        editTextgetmsg = rootView.findViewById(R.id.edit_text_message);
        IB_attacment = rootView.findViewById(R.id.imageButton_attacment);
        IB_send = rootView.findViewById(R.id.imageButton_send);
        tv_status = rootView.findViewById(R.id.tv_status);

        mProgressBar = rootView.findViewById(R.id.progressBar2);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);
        recyclerview_chat.setLayoutManager(mLayoutManager);
        recyclerview_chat.setHasFixedSize(true);


        currentUserID = PrefManager.getUserID(getActivity());
        finalNodeId = getNodeID();

        database = FirebaseDatabase.getInstance().getReference("chat_room")
                .child(finalNodeId).child("Massages");

        databaseRecent = FirebaseDatabase.getInstance().getReference("chat_room")
                .child(finalNodeId).child("recent");

        database.keepSynced(true);
        databaseReceiverUser = FirebaseDatabase.getInstance().getReference("User")
                .child(messageReceiverType + "_" + msgReceiverID);


        userDataValueEvent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                receiverUserFirebaseModel = dataSnapshot.getValue(UserFirebaseModel.class);
                if (receiverUserFirebaseModel != null) {
                    if (receiverUserFirebaseModel.isUserStatus()) {
                        tv_status.setText("Online");
                        tv_status.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
                    } else {


                        CharSequence timeago = DateUtils.getRelativeTimeSpanString(Long.parseLong(receiverUserFirebaseModel.getUserLastLogin()), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS);
                        tv_status.setText(timeago);
                        if (timeago.equals("In 0 minutes") || timeago.equals("0 minutes ago")) {
                            tv_status.setText("Offline");
                        }
                        tv_status.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                    }

                    if (!receiverUserFirebaseModel.isActive()) {
                        tv_status.setText("InActive");
                        tv_status.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                    }
                } else {
                    tv_status.setText("InActive");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReceiverUser.addValueEventListener(userDataValueEvent);


        videoDataBase = FirebaseStorage.getInstance().getReference().child("Inbox_Chat_Video");
        imageDataBase = FirebaseStorage.getInstance().getReference().child("Inbox_Chat_Images");
        documentDataBase = FirebaseStorage.getInstance().getReference().child("Inbox_Chat_Documents");

        chattingAdapter = new NewChattingAdapter(chattingList, mContext);


        recyclerview_chat.setAdapter(chattingAdapter);
        GetDataFirebase();


        editTextgetmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith(" ")) {
                    IB_send.setEnabled(false);
                    editTextgetmsg.setError("space at start is not allowed");

                } else {
                    IB_send.setEnabled(true);
                }
            }
        });

        IB_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!list_blocked_usersid.contains(currentUserID)) {

                    if (!editTextgetmsg.getText().toString().equals("")) {

                        String msg = editTextgetmsg.getText().toString().trim();
                        String id = getMassageID();
                        String time = getDateTime();
                        String username = PrefManager.getUserName(getContext());

                        ChattingMassageModel model = new ChattingMassageModel(id, time, PrefManager.getUserType(getContext()), currentUserID, username, msgReceiverID, messageReceiverType, false, Constant.MASSAGE, msg);

                        addRecentData(time, msg);

                        editTextgetmsg.setText("");
                        database.push().setValue(model);


                        sendNotification(msg);


                    } else {

                        editTextgetmsg.setError("Field Can't be empty..");
                    }


                } else {

                    Toast.makeText(mContext, "You can't discusse with this user anymore", Toast.LENGTH_SHORT).show();
                }
            }
        });


        IB_attacment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //view.showContextMenu();
                openFileChooser();
            }
        });


        return rootView;
    }

    private void sendNotification(String msg) {
        if (receiverUserFirebaseModel == null) {
            return;
        }

        RestClient.getClient().sendChatNotification(receiverUserFirebaseModel.getUserFCM(), PrefManager.getUserName(getContext()), receiverUserFirebaseModel.getUserID(), msg).enqueue(new Callback<ChatNotificationSendResponce>() {
            @Override
            public void onResponse(Call<ChatNotificationSendResponce> call, Response<ChatNotificationSendResponce> response) {

            }

            @Override
            public void onFailure(Call<ChatNotificationSendResponce> call, Throwable t) {

            }
        });

    }


    //*************************************************************** menu ****************************************
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.attacment_menu, menu);
        menu.setHeaderTitle("Select");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.location_item) {

            // locationPlacesIntent();

        } else if (item.getItemId() == R.id.video_item) {

            openFileChooservideo();
        } else if (item.getItemId() == R.id.image_item) {

            openFileChooser();

        } else if (item.getItemId() == R.id.document_item) {
            getPDF();

        } else {
            return false;
        }
        return true;
    }


    //************************************************************* here Firebase *************************************

    private void GetDataFirebase() {

        VEL_message = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chattingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        ChattingMassageModel data = snapshot.getValue(ChattingMassageModel.class);
                        assert data != null;
                        chattingList.add(data);

                        recyclerview_chat.scrollToPosition(chattingList.size() - 1);

                        if (!data.getSenderID().equalsIgnoreCase(currentUserID)) {
                            snapshot.child("read").getRef().setValue(true);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                chattingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        database.addValueEventListener(VEL_message);

        recentmassageValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recentModel = dataSnapshot.getValue(RecentMassageModel.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseRecent.addValueEventListener(recentmassageValueEventListener);


    }


    private void uploadImageFile() {


        mProgressBar.setVisibility(View.VISIBLE);
        if (mImageUri != null) {

            final StorageReference imgReference = imageDataBase.child(mImageUri.getLastPathSegment());
            UploadTask uploadTask = imgReference.putFile(mImageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return imgReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri taskResult = task.getResult();
                        String time = getDateTime();
                        ChattingMassageModel ch = new ChattingMassageModel(getMassageID(), time, PrefManager.getUserType(getContext()), currentUserID, PrefManager.getUserName(getContext()), msgReceiverID, messageReceiverType, false, Constant.IMAGE, taskResult.toString());

                        database.push().setValue(ch);
                        addRecentData(time, "Image");

                        sendNotification("Image");

                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    public void addRecentData(String time, String msg) {
        String[] ids = finalNodeId.split("_");
        String user1ID = null;
        String user2ID = null;
        String user1Name = null;
        String user2Name = null;

        if (ids[1].equalsIgnoreCase(currentUserID)) {
            user1ID = currentUserID;
            user1Name = PrefManager.getUserName(getActivity());
            user2Name = getMsgReceiverName;
            user2ID = msgReceiverID;
        } else {
            user1Name = getMsgReceiverName;
            user1ID = msgReceiverID;

            user2ID = currentUserID;
            user2Name = PrefManager.getUserName(getActivity());
        }


        RecentMassageModel rs = new RecentMassageModel(time, user1Name, user2Name, user1ID, user2ID, msg);
        databaseRecent.setValue(rs);
    }

    private void uploadVideoFile() {

        mProgressBar.setVisibility(View.VISIBLE);
        if (selectedvideo != null) {

            final StorageReference videoReference = videoDataBase.child(selectedvideo.getLastPathSegment());
            UploadTask uploadTask = videoReference.putFile(selectedvideo);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return videoReference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String time = getDateTime();
                                Uri taskResult = task.getResult();
                                ChattingMassageModel ch = new ChattingMassageModel(getMassageID(), time, PrefManager.getUserType(getContext()), currentUserID, PrefManager.getUserName(getContext()), msgReceiverID, messageReceiverType, false, Constant.VIDEO, taskResult.toString());
                                database.push().setValue(ch);
                                addRecentData(time, "Video");
                               /* Sendnotificationrequest1("Video");
                                getnotificationresponse();*/
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPdfFile(Uri data) {

        mProgressBar.setVisibility(View.VISIBLE);

        final StorageReference sRef = documentDataBase.child(System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if (taskSnapshot != null) {

                            sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    String url = task.getResult().toString();
                                    String time = getDateTime();
                                    ChattingMassageModel ch = new ChattingMassageModel(getMassageID(), time, PrefManager.getUserType(getContext()), currentUserID, PrefManager.getUserName(getContext()), msgReceiverID, messageReceiverType, false, Constant.DOCUMENT, url);
                                    database.push().setValue(ch);
                                    addRecentData(time, "Pdf");
                                   /* Sendnotificationrequest1("Pdf Document");
                                    getnotificationresponse();*/
                                    mProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }

   /* public void uploadLocationToFirebase(Intent data) {
        Place place = getPlace(mContext, data);
        if (place != null) {
            LatLng latLng = place.getLatLng();
            String time = getDateTime();
            ChattingMassageModel ch = new ChattingMassageModel(getMassageID(), time, currentUserID, PrefManager.getUserName(getContext()), msgReceiverID, false, Constant.LOCATION, latLng.latitude, latLng.longitude);
            database.push().setValue(ch).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    */
   /*Sendnotificationrequest1("Location");
                    getnotificationresponse();*/
   /*
                }
            });
            addRecentData(time, "Location");
        } else {
            Log.e("Chat", "onActivityResult: ==" + "place is null");
        }
    }*/

    //************************************************************* intents methods *************************************


    private void getPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + mContext.getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf Document"), PICK_PDF_CODE);
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openFileChooservideo() {
        Intent intent1 = new Intent();
        intent1.setType("video/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent1, "Select a Video "), PICK_VIDEO_REQUEST);

    }

    //***************************************** on Activty Result method*******************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            try {
                actualImage = FileUtils.from(mContext, data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }


          /*  if (actualImage != null) {
                try {
                    compressedImage = new Compressor(mContext)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(actualImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

            mImageUri = Uri.fromFile(actualImage);

            uploadImageFile();

        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                //uploadLocationToFirebase(data);
            }
        } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK) {

            selectedvideo = data.getData();

            if (selectedvideo != null) {
                uploadVideoFile();
            }


        } else if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                uploadPdfFile(data.getData());
            } else {
                Toast.makeText(mContext, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getMsgReceiverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateUser(getContext(), true, "0", true);

        active = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        active = false;
        updateUser(getContext(), active, "0", true);

    }


    //getting massage id for
    private String getMassageID() {
        Date d = new Date();
        SimpleDateFormat dl = new SimpleDateFormat("ddMMyyyyhhmmssSSS");
        return dl.format(d);
    }

    // geting date and time
    private String getDateTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    // creating node id
    private String getNodeID() {
        StringBuffer sb = new StringBuffer();
        int i = msgReceiverID.compareTo(currentUserID);

        if (i < 0) {
            sb.append(PrefManager.getUserType(getContext())).append("_").append(currentUserID)
                    .append("_")
                    .append(messageReceiverType).append("_").append(msgReceiverID);
        } else if (i > 0) {
            sb.append(messageReceiverType).append("_").append(msgReceiverID)
                    .append("_")
                    .append(PrefManager.getUserType(getContext())).append("_").append(currentUserID);
        } else {

            if (messageReceiverType.equals("4")) {
                sb.append(messageReceiverType).append("_").append(msgReceiverID)
                        .append("_")
                        .append(PrefManager.getUserType(getContext())).append("_").append(currentUserID);
            } else {
                sb.append(PrefManager.getUserType(getContext())).append("_").append(currentUserID)
                        .append("_")
                        .append(messageReceiverType).append("_").append(msgReceiverID);
            }

        }


        return new String(sb);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (database != null && VEL_message != null) {
            database.removeEventListener(VEL_message);
            database.removeEventListener(recentmassageValueEventListener);
            databaseReceiverUser.removeEventListener(userDataValueEvent);
        }
        active = false;
        updateUser(getContext(), active, "0", true);
    }


}
