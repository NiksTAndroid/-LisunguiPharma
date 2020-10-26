package com.lisungui.pharma.activities;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lisungui.pharma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessagingActivity extends AppCompatActivity {

    private Context context;
    private ImageButton sendMessageImageButton;
    private EditText messageEditText;
    private RecyclerView messagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Messaging");

        initValues();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initValues()
    {
        context = MessagingActivity.this;
        sendMessageImageButton = (ImageButton)findViewById(R.id.message_send_button);
        messageEditText = (EditText)findViewById(R.id.message_edittext);
        messagesRecyclerView = (RecyclerView)findViewById(R.id.messages_recyclerview);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //final ArrayList<String> messagesList = new ArrayList(Arrays.asList("Hi", "Hello", "How are you?", "Fine thank you"));
        final ArrayList<String> messagesList = new ArrayList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        final MessagesListRecyclerViewAdapter messagesListRecyclerViewAdapter = new MessagesListRecyclerViewAdapter(context, messagesList);
        messagesRecyclerView.setAdapter(messagesListRecyclerViewAdapter);

        sendMessageImageButton.setEnabled(false);

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!messageEditText.getText().toString().isEmpty() || !messageEditText.getText().toString().equalsIgnoreCase(""))
                    sendMessageImageButton.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendMessageImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!messageEditText.getText().toString().isEmpty() || !messageEditText.getText().toString().equalsIgnoreCase("")) {
                    messagesList.add(messageEditText.getText().toString());
                    messageEditText.setText("");
                    messagesRecyclerView.smoothScrollToPosition(messagesRecyclerView.getAdapter().getItemCount()-1);
                    messagesListRecyclerViewAdapter.notifyItemInserted(messagesList.size());
                }
            }
        });
    }

    class MessagesListRecyclerViewAdapter extends RecyclerView.Adapter
    {
        private Context context;
        private ArrayList<String> messagesList;
        private static final int VIEW_SENT_MESSAGE = 1;
        private static final int VIEW_RECEIVED_MESSAGE = 2;

        public MessagesListRecyclerViewAdapter(Context context, ArrayList<String> messagesList)
        {
            this.context = context;
            this.messagesList = messagesList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item_layout, parent, false);
            //return null;
            //return new MessagesListViewHolder(view);

            View view;
            if(viewType == VIEW_SENT_MESSAGE)
            {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item_layout, parent, false);
                TextView timeTextView = view.findViewById(R.id.time_textview);
                //new SimpleDateFormat("dd/mm/yyyy hh:mm").format((System.currentTimeMillis()));
                timeTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format((System.currentTimeMillis())));

                return new SenderMessagesListViewHolder(view);
            }
            else if(viewType == VIEW_RECEIVED_MESSAGE)
            {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item_layout, parent, false);
                TextView timeTextView = view.findViewById(R.id.time_textview);
                timeTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format((System.currentTimeMillis())));
                return new ReceiverMessagesListViewHolder(view);
            }

            return null;
        }

        @Override
        //public void onBindViewHolder(MessagesListViewHolder holder, int position) {
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            //holder.messageTextView.setText((CharSequence) messagesList.get(position));

            switch (holder.getItemViewType())
            {
                case VIEW_SENT_MESSAGE:
                    ((SenderMessagesListViewHolder)holder).messageTextView.setText(messagesList.get(position));
                    ((SenderMessagesListViewHolder)holder).messageTextView.setVisibility(View.VISIBLE);

                    break;
                case VIEW_RECEIVED_MESSAGE:
                    ((ReceiverMessagesListViewHolder)holder).messageTextView.setText(messagesList.get(position));
                    ((ReceiverMessagesListViewHolder)holder).messageTextView.setVisibility(View.VISIBLE);

                    break;
            }
            return;
        }

        @Override
        public int getItemCount() {

            //return 0;
            return messagesList.size();
        }

        @Override
        public int getItemViewType(int position) {

            if(messagesList.get(position).equalsIgnoreCase("fine"))
                return VIEW_RECEIVED_MESSAGE;
            else
                return VIEW_SENT_MESSAGE;
            //return super.getItemViewType(position);

        }

        class MessagesListViewHolder extends RecyclerView.ViewHolder {
            TextView messageTextView;
            public MessagesListViewHolder(View itemView) {
                super(itemView);
                //messageTextView = (TextView)itemView.findViewById(R.id.messages_textview);
            }
        }

        class SenderMessagesListViewHolder extends RecyclerView.ViewHolder {
            TextView messageTextView;
            public SenderMessagesListViewHolder(View itemView) {
                super(itemView);
                messageTextView = itemView.findViewById(R.id.messages_textview_1);
            }
        }

        class ReceiverMessagesListViewHolder extends RecyclerView.ViewHolder {
            TextView messageTextView;
            public ReceiverMessagesListViewHolder  (View itemView) {
                super(itemView);
                messageTextView = itemView.findViewById(R.id.messages_textview_2);
            }
        }

    }
}