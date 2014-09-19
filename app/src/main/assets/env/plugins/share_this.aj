import android.content.*;

editAPI.addEditorButton("share", new Runnable() {
 public void run() {
   Intent sendIntent = new Intent();
   sendIntent.setAction(Intent.ACTION_SEND);
   sendIntent.putExtra(Intent.EXTRA_TEXT, codeEditText.getText().toString());
   sendIntent.setType("text/plain");
   ctx.startActivity(sendIntent);
 }
});
