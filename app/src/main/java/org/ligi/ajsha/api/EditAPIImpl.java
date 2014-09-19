package org.ligi.ajsha.api;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EditAPIImpl implements EditAPI {

    private final Context context;
    private final ViewGroup buttonContainer;

    public EditAPIImpl(Context context, ViewGroup buttonContainer) {
        this.context = context;
        this.buttonContainer = buttonContainer;
    }

    @Override
    public void addEditorButton(String text, final Runnable onClickAction) {

        Button clearBtn = new Button(context);
        clearBtn.setText(text);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickAction.run();
            }
        });
        buttonContainer.addView(clearBtn);
    }
}
