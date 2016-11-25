package kmods;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.preference.Preference;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import static kmods.Utils.getResID;

public class ActionP extends Preference {
    public ActionP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ActionP(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ActionP(Context context) {
        super(context);
    }
    @Override
    protected void onClick() {
        super.onClick();
        switch (getKey()) {
            case "cfu":
                new Update(getContext()).execute((String[]) new String[0]);
                break;
            case "reset":
                getContext().getSharedPreferences("kmods_privacy", 0).edit().clear().apply();
                break;
            case "backup":
                if (new File(Environment.getDataDirectory(), "data/com.whatsapp").exists()) {
                    if (!new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup").exists()){
                        new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup").mkdir();
                    }
                    if (!new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp").exists()){
                        new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp").mkdir();
                    }
                    new CopyTask(getContext(), true, new File(Environment.getDataDirectory(), "data/com.whatsapp"), new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp")).execute(new File[0]);
                } else {
                    Toast.makeText(getContext(), "Can't find a Data!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "restore":
                if (new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup").exists() && new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp").exists()) {
                    new CopyTask(getContext(), false, new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp"), new File(Environment.getDataDirectory(), "data/com.whatsapp")).execute(new File[0]);
                } else {
                    Toast.makeText(getContext(), "Can't find a backup in '/sdcard/WhatsApp/KBackup'!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "share":
                final String string = getContext().getString(getResID("ShareBdy", "string"));
                final Intent intent3 = new Intent("android.intent.action.SEND");
                intent3.setType("text/plain");
                intent3.putExtra("android.intent.extra.SUBJECT", getContext().getString(getResID("ShareSbj", "string")));
                intent3.putExtra("android.intent.extra.TEXT", string);
                getContext().startActivity(Intent.createChooser(intent3, getContext().getString(getResID("Share", "string"))));
                break;
            case "report":
                final Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("message/rfc822");
                intent2.putExtra("android.intent.extra.EMAIL", new String[] { "patel.kuldip91@gmali.com" });
                intent2.putExtra("android.intent.extra.SUBJECT", "KWhatsApp v" + Utils.v1 + "." + Utils.v2);
                intent2.putExtra("android.intent.extra.TEXT", "");
                try {
                    getContext().startActivity(Intent.createChooser(intent2,"Report..."));
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Can't find email client.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "clemoji":
                final File file = new File("/data/data/com.whatsapp/files/emoji");
                if (file.exists()) {
                    file.delete();
                    Toast.makeText(getContext(), "All Recent Emojis Cleared", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No Recent Emojis There!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "credits":
                AlertDialog.Builder alertDialog;
                alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Credits");
                WebView wv = new WebView(getContext());
                wv.loadUrl("file:///android_asset/credits.html");
                alertDialog.setView(wv);
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case "openchat":
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New Chat");
                builder.setMessage("Enter Number");
                final EditText input = new EditText(getContext());
                input.setText("0");
                builder.setView(input);
                builder.setPositiveButton("Message!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String number = input.getText().toString() + "@s.whatsapp.net";
                                if(Utils.OpenChat(number) == null){
                                    Toast.makeText(getContext(),"This Number not Exist On WhatsApp, Check Again!", Toast.LENGTH_SHORT).show();
                                } else {
                                    getContext().startActivity(Utils.OpenChat(number));
                                }
                            }
                        });
                builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                builder.show();
                break;
        }
    }
}