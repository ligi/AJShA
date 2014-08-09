import android.view.*;
import android.widget.*;

Button clearBtn = new Button(ctx);
clearBtn.setText("clear");
clearBtn.setOnClickListener(new View.OnClickListener() {
  public void onClick(View v) {
   codeEditText.setText("");
  }
});
buttonContainer.addView(clearBtn);