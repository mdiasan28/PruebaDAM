package ma.content.tarea4_2;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;

public class MenuPreferencias extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new FragmentoPreferencias()).commit();
    }

    public static class FragmentoPreferencias extends PreferenceFragment {
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.mis_preferencias);
        }
    }

    public void volver (View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
