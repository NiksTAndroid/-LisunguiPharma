# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/siddeshwar/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** e(...);
}

-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# OkHttp rules
-dontwarn okio.**
#-dontwarn com.squareup.okhttp.**
#-keep class com.squareup.okhttp3.** { *; }
#-keep interface com.squareup.okhttp.** { *; }

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

#Retrofit
-dontwarn retrofit2.Platform$Java8
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.* <methods>;
}

-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

#-keep class com.yuvraj.esampark.** { *; }
-keep class com.lisungui.pharma.models.** { *; }


# Keep GSON stuff
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# Keep Picasso
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class * {
    @com.squareup.picasso.** *;
}