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

# Compose
-assumenosideeffects class androidx.compose.runtime.ComposerKt {
    public static void sourceInformation(androidx.compose.runtime.Composer, java.lang.String);
    public static void sourceInformationMarkerStart(androidx.compose.runtime.Composer, int, java.lang.String);
    public static void sourceInformationMarkerEnd(androidx.compose.runtime.Composer);
}


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

# Data classes
-keepclassmembers class com.yukinoa.domain.model.** {
    <init>(...);
}
-keepclassmembers class com.yukinoa.data.model.** {
    <init>(...);
}

# Keep all the data class fields
-keepclassmembers class com.yukinoa.domain.model.** {
    *** *;
}
-keepclassmembers class com.yukinoa.data.model.** {
    *** *;
}

# Keep all the repository implementations
-keep class com.yukinoa.data.repository.** {*;}
-keep interface com.yukinoa.domain.repository.** {*;}

# Keep all the use cases
-keep class com.yukinoa.domain.usecase.** {*;}

# Keep all the ViewModels
-keep class com.yukinoa.presentation.viewmodel.** {*;}

# Keep all the UI components
-keep class com.yukinoa.presentation.ui.** {*;}

# Keep all the navigation components
-keep class com.yukinoa.presentation.navigation.** {*;}

# Keep all the database classes
-keep class com.yukinoa.data.database.** {*;}

# Keep all the converter classes
-keep class com.yukinoa.data.converter.** {*;}

# Keep all the theme classes
-keep class com.yukinoa.presentation.theme.** {*;}