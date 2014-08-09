import android.view.*;

Button shareBtn = new Button(ctx);
shareBtn.setText("share");
shareBtn.setOnClickListener(new View.OnClickListener() {
  public void onClick(View v) {
   Intent sendIntent = new Intent();
   sendIntent.setAction(Intent.ACTION_SEND);
   sendIntent.putExtra(Intent.EXTRA_TEXT, codeEditText.getText().toString());
   sendIntent.setType("text/plain");
   ctx.startActivity(sendIntent);
  }
});
buttonContainer.addView(shareBtn);