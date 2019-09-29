package com.absinthe.anywhere_.ui.about;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.absinthe.anywhere_.BuildConfig;
import com.absinthe.anywhere_.R;
import com.drakeet.about.AbsAboutActivity;
import com.drakeet.about.Card;
import com.drakeet.about.Category;
import com.drakeet.about.Contributor;
import com.drakeet.about.License;

import java.util.List;

public class AboutActivity extends AbsAboutActivity {

    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.drawable.splash);
        slogan.setText(getString(R.string.slogan));
        version.setText(String.format("Version: %s", BuildConfig.VERSION_NAME));
    }

    @Override
    protected void onItemsCreated(@NonNull List<Object> items) {
        items.add(new Category("帮助"));
        items.add(new Card(getString(R.string.help_text)));

        items.add(new Category("Developers"));
        items.add(new Contributor(R.drawable.rabbit, "Absinthe", "Developer & designer", "coolmarket://www.coolapk.com/u/482045"));

        items.add(new Category("Open Source Licenses"));
        items.add(new License("Shizuku", "Rikka", "License", "https://github.com/RikkaApps/Shizuku"));
        items.add(new License("FreeReflection", "tiann", License.MIT, "https://github.com/tiann/FreeReflection"));
        items.add(new License("MultiType", "drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"));
        items.add(new License("about-page", "drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"));
        items.add(new License("Material Tap Target Prompt", "sjwall", License.APACHE_2, "https://github.com/sjwall/MaterialTapTargetPrompt"));
        items.add(new License("Glide", "bumptech", "License", "https://github.com/bumptech/glide"));
        items.add(new License("AndroidX", "Google", License.APACHE_2, "https://source.google.com"));
        items.add(new License("Android Jetpack", "Google", License.APACHE_2, "https://source.google.com"));

    }
}
