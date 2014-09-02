import android.content.pm.PackageManager;

int res;

res=ctx.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
print("WRITE_EXTERNAL_STORAGE:" +  (res== PackageManager.PERMISSION_GRANTED));

res=ctx.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE");
print("ACCESS_NETWORK_STATE:" +  (res== PackageManager.PERMISSION_GRANTED));


