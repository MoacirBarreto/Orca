package devandroid.moacir.orca

import android.app.Application
import java.util.Locale

class OrcaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Define o Locale padrão para o aplicativo inteiro como Português (Brasil)
        // Isso afetará todos os componentes que usam Locale, como o DatePicker.
        Locale.setDefault(Locale("pt", "BR"))
    }
}

