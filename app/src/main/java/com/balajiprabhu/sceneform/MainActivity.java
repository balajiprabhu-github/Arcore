package com.balajiprabhu.sceneform;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private TransformableNode transformableNode;
    private Button redButton,yellowButton,blueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redButton = findViewById(R.id.redColorButton);
        yellowButton = findViewById(R.id.yellowColorButton);
        blueButton = findViewById(R.id.blueColorButton);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            Anchor anchor = hitResult.createAnchor();

            ModelRenderable.builder()
                    .setSource(this, Uri.parse("mustang_GT.sfb"))
                    .build().thenAccept(modelRenderable -> addModelToScene(anchor, modelRenderable))
                    .exceptionally(throwable -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(throwable.getMessage()).show();
                        return null;
                    });

        });


        redButton.setOnClickListener(view -> changeRedColor());
        yellowButton.setOnClickListener(view -> changeYellowColor());
        blueButton.setOnClickListener(view -> changeBlueColor());
    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderables) {

        modelRenderable = modelRenderables;

        AnchorNode anchorNode = new AnchorNode(anchor);
        transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        transformableNode.setLocalRotation(Quaternion.axisAngle(new Vector3(1f, 0, 0), 270f));
        transformableNode.getScaleController().setMaxScale(1.01f);
        transformableNode.getScaleController().setMinScale(0.01f);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();

    }

    private void changeYellowColor(){

        Renderable tintedRenderable = modelRenderable.makeCopy();
        tintedRenderable.getMaterial().setFloat4("baseColorTint", 2.0f, 1.0f, 0.0f, 1.0f);
        transformableNode.setRenderable(tintedRenderable);

    }

    private void changeRedColor(){

        Renderable tintedRenderable = modelRenderable.makeCopy();
        tintedRenderable.getMaterial().setFloat4("baseColorTint", 1.0f, 0.0f, 0.0f, 1.0f);
        transformableNode.setRenderable(tintedRenderable);

    }

    private void changeBlueColor(){

        Renderable tintedRenderable = modelRenderable.makeCopy();
        tintedRenderable.getMaterial().setFloat4("baseColorTint", 1.0f, 1.0f, 0.0f, 2.0f);
        transformableNode.setRenderable(tintedRenderable);

    }




}
