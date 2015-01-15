package fi.immonen.kalle.rgbstormer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

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
        private OpacityBar mBarFade;
        private Button mButton;
        private OpacityBar mBarSteps;
        private OpacityBar mBarDelay;

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


                    mBarFade = (OpacityBar) rootView.findViewById(R.id.bar_fade);
                    mBarFade.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
                        @Override
                        public void onOpacityChanged(int i) {
                            mCallback.onFadeSet(i);
                        }
                    });

                    mBarDelay = (OpacityBar) rootView.findViewById(R.id.bar_delay);
                    mBarDelay.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
                        @Override
                        public void onOpacityChanged(int i) {
                            mCallback.onDelaySet(i);
                        }
                    });
                    mBarSteps = (OpacityBar) rootView.findViewById(R.id.bar_steps);
                    mBarSteps.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
                        @Override
                        public void onOpacityChanged(int i) {
                            mCallback.onStepsSet(i);
                        }
                    });
//                    mBarSteps.setColorPicker(mColorPicker);
//                    mBarFade.setColorPicker(mColorPicker);
                    mColorPicker.setEnabled(false);


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

                    final NumberPicker numberPicker = (NumberPicker) rootView.findViewById(R.id.picker_active_mode);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(11);
                    numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                            mCallback.onModeActivated(i);
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

        public void setColorWheelEnabled(boolean bool) {
            mColorPicker.setEnabled(bool);
        }


        public interface OnFragmentInteractionListener {
            public void onColorSelected(int color);

            public void buttonClicked();

            public void onModeActivated(int mode);

            public void onFadeSet(int fade);

            public void onDelaySet(int delay);

            public void onStepsSet(int steps);
        }
    }
}