# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces:
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Hilt - Keep all generated classes
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class com.yukinoa.data.DataModule { *; }
-keep class com.yukinoa.data.DataModule_* { *; }
-keep class com.yukinoa.note.DaggerNoteApplication_HiltComponents_* { *; }
-keep class com.yukinoa.note.Hilt_* { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keepclassmembers class androidx.room.RoomSQLiteQuery {
    public static androidx.room.RoomSQLiteQuery **(java.lang.String, int);
}

# ViewModel
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(androidx.lifecycle.SavedStateHandle);
}

# Navigation
-keepclassmembers class * extends androidx.navigation.NavArgs {
    <init>(...);
}
-keep class androidx.navigation.compose.** { *; }

# Compose
-assumenosideeffects class androidx.compose.runtime.ComposerKt {
    public static void sourceInformation(androidx.compose.runtime.Composer, java.lang.String);
    public static void sourceInformationMarkerStart(androidx.compose.runtime.Composer, int, java.lang.String);
    public static void sourceInformationMarkerEnd(androidx.compose.runtime.Composer);
}
-dontwarn androidx.compose.runtime.**

# Material icons
-keep class androidx.compose.material.icons.** { *; }
-keep class androidx.compose.material.icons.Icons$AutoMirrored$Filled { *; }

# Core classes that need to be kept
-keep class com.yukinoa.core.BaseViewModel { *; }
-keep class com.yukinoa.core.Result { *; }
-keep class com.yukinoa.core.Result$* { *; }

# Add rules to suppress warnings for missing classes
-dontwarn com.yukinoa.core.BaseViewModel
-dontwarn com.yukinoa.core.Result$Error
-dontwarn com.yukinoa.core.Result$Loading
-dontwarn com.yukinoa.core.Result$Success
-dontwarn com.yukinoa.core.Result