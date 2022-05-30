package com.niceplaces.niceplaces.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.BuildConfig
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.dialogs.RateDialog
import com.niceplaces.niceplaces.utils.AppRater
import java.util.*




class MenuActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1
    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton: Button
    private lateinit var TVUsername: TextView
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar!!.hide()
        val thisActivity = this
        FirebaseAnalytics.getInstance(this)
        val placesNearYou = findViewById<Button>(R.id.btn_places_near_you)
        val explore = findViewById<Button>(R.id.btn_virtual_tour)
        val newPlaces = findViewById<Button>(R.id.btn_new_places)
        val quiz = findViewById<Button>(R.id.btn_quiz)
        //ImageView IVlogin = findViewById(R.id.imageview_login);
        val IVInfo = findViewById<ImageView>(R.id.imageview_info)
        val IVNotifications = findViewById<ImageView>(R.id.imageview_notifications)
        val IVWeb = findViewById<ImageView>(R.id.imageview_web)
        val IVInstagram = findViewById<ImageView>(R.id.imageview_instagram)
        val IVFacebook = findViewById<ImageView>(R.id.imageview_facebook)
        val IVTwitter = findViewById<ImageView>(R.id.imageview_twitter)
        val IVDebug = findViewById<ImageView>(R.id.imageview_debug)
        val buttonVisited = findViewById<Button>(R.id.button_visited)
        val buttonWished = findViewById<Button>(R.id.button_wished)
        val buttonFav = findViewById<Button>(R.id.button_fav)
        val textViewPrivacy = findViewById<TextView>(R.id.textview_privacy_policy)
        /*signInButton = findViewById(R.id.sign_in_button)
        signOutButton = findViewById(R.id.sign_out_button)
        TVUsername = findViewById(R.id.tv_username)
        IVlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, LoginActivity.class);
                startActivity(intent);
            }
        });
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
        signInButton.setOnClickListener {
            signIn()
        }
        signOutButton.setOnClickListener {
            signOut()
        }*/
        IVInfo.setOnClickListener {
            val intent = Intent(thisActivity, InfoActivity::class.java)
            startActivity(intent)
        }
        IVNotifications.setOnClickListener {
            val intent = Intent(thisActivity, NotificationsActivity::class.java)
            startActivity(intent)
        }
        IVWeb.setOnClickListener {
            var url = Const.WEBSITE_EN_URL
            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                url = Const.WEBSITE_URL
            }
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }
        IVInstagram.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(Const.INSTAGRAM))
            startActivity(i)
        }
        IVFacebook.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(Const.FACEBOOK))
            startActivity(i)
        }
        IVTwitter.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(Const.TWITTER))
            startActivity(i)
        }
        placesNearYou.setOnClickListener {
            val intent = Intent(thisActivity, MapsActivity::class.java)
            startActivity(intent)
        }
        explore.setOnClickListener {
            val intent = Intent(thisActivity, ExploreActivity::class.java)
            startActivity(intent)
        }
        newPlaces.setOnClickListener {
            val intent = Intent(thisActivity, LatestPlacesActivity::class.java)
            startActivity(intent)
        }
        if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
            quiz.visibility = View.VISIBLE
            quiz.setOnClickListener {
                val intent = Intent(thisActivity, QuizActivity::class.java)
                startActivity(intent)
            }
        }
        buttonVisited.setOnClickListener {
            val intent = Intent(thisActivity, UserListActivity::class.java)
            intent.putExtra(UserListActivity.Companion.USERLIST, UserListActivity.Companion.VISITED)
            startActivity(intent)
        }
        buttonWished.setOnClickListener {
            val intent = Intent(thisActivity, UserListActivity::class.java)
            intent.putExtra(UserListActivity.Companion.USERLIST, UserListActivity.Companion.WISHED)
            startActivity(intent)
        }
        buttonFav.setOnClickListener {
            val intent = Intent(thisActivity, UserListActivity::class.java)
            intent.putExtra(UserListActivity.Companion.USERLIST, UserListActivity.Companion.FAVOURITE)
            startActivity(intent)
        }
        textViewPrivacy.setOnClickListener {
            var url = "http://www.niceplaces.it/en/privacy"
            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                url = "http://www.niceplaces.it/privacy"
            }
            val i = Intent(Intent.ACTION_VIEW,
                    Uri.parse(url))
            startActivity(i)
        }
        if (BuildConfig.DEBUG) {
            IVDebug.setOnClickListener {
                val intent = Intent(thisActivity, DebugOptionsActivity::class.java)
                startActivity(intent)
            }
        } else {
            IVDebug.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (AppRater.needToTriggerRateDialog(this)){
            val dialog = RateDialog()
            dialog.show(supportFragmentManager, "rate_dialog")
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                signInButton.visibility = View.VISIBLE
                TVUsername.visibility = View.GONE
                signOutButton.visibility = View.GONE
            }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        signInButton.visibility = View.GONE
        if (account != null) {
            val personName = account.displayName
            val personGivenName = account.givenName
            val personFamilyName = account.familyName
            val personEmail = account.email
            val personId = account.id
            val personPhoto = account.photoUrl
            TVUsername.setText(personName)
            TVUsername.visibility = View.VISIBLE
        }
        signOutButton.visibility = View.VISIBLE
    }
}