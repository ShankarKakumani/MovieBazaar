@file:Suppress("DEPRECATION")

package com.movie.moviebazaar.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.movie.moviebazaar.R
import com.shankar.customtoast.Snacky
import com.shankar.customtoast.StatusBar
import com.shankar.customtoast.Toasty
import kotlinx.android.synthetic.main.activity_authentication.*

class Authentication : AppCompatActivity() {

    private val tag = "GoogleSignInActivity"
    private val rc_sign_in = 9001
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var pd : ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)


        pd = ProgressDialog(this,R.style.MyAlertDialogStyle)
        pd.setMessage("Loading ...")
        pd.setCancelable(false)
        StatusBar.setStatusBarColorCustom(this,R.color.black)

        googleSignIn.setOnClickListener { signIn() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.revokeAccess()

        auth = Firebase.auth


    }

    private fun signIn() {

        pd.show()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, rc_sign_in)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == rc_sign_in) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(tag, "firebaseAuthWithGoogle:" + account.id)
                pd.setMessage("Signing In ")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(tag, "Google sign in failed", e)
                Toasty.infoToast(this,"Google sign in failed")
                pd.dismiss()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@Authentication, HomeActivity::class.java))
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(tag, "signInWithCredential:failure", task.exception)
                        Snacky.basicSnack(this, "SignIn Failed")
                    }
                }
    
    }


    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

    }



}