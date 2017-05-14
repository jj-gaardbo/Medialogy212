
package layout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jensjakupgaardbo.medialogy212.R;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

public class eleventhInfoFragment extends Fragment implements ISlideBackgroundColorHolder {
    ViewGroup viewGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_eleventh_info, container, false);
        viewGroup = container;
        setBackgroundColor(getDefaultBackgroundColor());
        return v;
    }

    @Override
    public int getDefaultBackgroundColor() {
        // Return the default background color of the slide.
        return Color.parseColor("#000000");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        // Set the background color of the view within your slide to which the transition should be applied.
        if (viewGroup != null) {
            viewGroup.setBackgroundColor(backgroundColor);
        }
    }
}
