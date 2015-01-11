package fi.immonen.kalle.rgbstormer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import fi.immonen.kalle.rgbstormer.MainActivity;
import fi.immonen.kalle.rgbstormer.R;

/**
 * Created by TeZla on 7.1.2015.
 */
public class Fragments {
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private OnFragmentInteractionListener mCallback;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ColorPicker mColorPicker;
        private OpacityBar mOpacityBar;
        private Button mButton;
        private SVBar mSvBar;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            switch (position) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_main_control, container, false);
                    mColorPicker = (ColorPicker) rootView.findViewById(R.id.color_picker);
                    mButton = (Button) rootView.findViewById(R.id.button_pick_color);
                    mOpacityBar = (OpacityBar) rootView.findViewById(R.id.opacitybar);
                    mSvBar = (SVBar) rootView.findViewById(R.id.svbar);
//                    mSvBar.setColorPicker(mColorPicker);
//                    mOpacityBar.setColorPicker(mColorPicker);


                    mColorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
                        @Override
                        public void onColorChanged(int color) {
                            mColorPicker.setOldCenterColor(color);
                            mCallback.onColorSelected(color);
                        }
                    });

                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCallback.buttonClicked();
                        }
                    });

                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

            try {
                mCallback = (OnFragmentInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implment OnFragmentIntecratcioListneter!");
            }
        }


        public interface OnFragmentInteractionListener {
            public void onColorSelected(int color);
            public void buttonClicked();
        }
    }
}