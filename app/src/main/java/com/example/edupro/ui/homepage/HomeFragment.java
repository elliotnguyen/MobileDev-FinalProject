package com.example.edupro.ui.homepage;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentHomeBinding;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
//    private KonfettiView konfettiView = null;
//    private Shape.DrawableShape drawableShape = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final Drawable drawable =
//                ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart);
//        if (drawable != null) {
//            drawableShape = new Shape.DrawableShape(
//                    drawable,
//                    true, true);
//        }

//        final Drawable drawable =
//                ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart);
        //drawableShape = ImageUtil.loadDrawable(drawable, true, true);

//        konfettiView = root.findViewById(R.id.konfettiView);
//        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(30);
//        konfettiView.start(
//                new PartyFactory(emitterConfig)
//                        .angle(Angle.RIGHT - 45)
//                        .spread(Spread.SMALL)
//                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
//                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
//                        .setSpeedBetween(10f, 30f)
//                        .position(new Position.Relative(0.0, 0.5))
//                        .build(),
//                new PartyFactory(emitterConfig)
//                        .angle(Angle.LEFT + 45)
//                        .spread(Spread.SMALL)
//                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
//                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
//                        .setSpeedBetween(10f, 30f)
//                        .position(new Position.Relative(1.0, 0.5))
//                        .build());

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}