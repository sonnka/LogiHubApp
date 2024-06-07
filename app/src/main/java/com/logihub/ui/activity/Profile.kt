package com.logihub.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.logihub.R
import com.logihub.model.response.TruckManagerDTO
import com.logihub.service.ApiServiceImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : Fragment() {

    private val activity: Fragment = this@Profile
    private lateinit var token: String
    private var userId: Long = 0L

    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var email: TextView
    private lateinit var company: TextView

    private val apiService = ApiServiceImpl()
    private lateinit var editUserButton: MaterialButton
    private lateinit var signout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.activity_profile, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        userId = MainPage.getUserId()!!
        token = MainPage.getToken().toString()

        firstName = v.findViewById(R.id.firstNameText)
        lastName = v.findViewById(R.id.lastNameText)
        email = v.findViewById(R.id.emailText)
        company = v.findViewById(R.id.companyText)

        editUserButton = v.findViewById(R.id.editUserButton)
        signout = v.findViewById(R.id.singOut)

        editUserButton.let { e ->
            e.setOnClickListener {
                editUser()
            }
        }

        signout.let { s ->
            s.setOnClickListener {
                logout()
            }
        }

        getUser()
    }

    private fun getUser() {
        apiService.getManager("Bearer " + token, userId, object :
            Callback<TruckManagerDTO> {
            override fun onResponse(
                call: Call<TruckManagerDTO>,
                response: Response<TruckManagerDTO>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        fillData(user)
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<TruckManagerDTO>, t: Throwable) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fillData(user: TruckManagerDTO) {
        firstName.text = user.firstName
        lastName.text = user.lastName
        email.text = email.text.toString() + ": " + user.email
        company.text = company.text.toString() + ": " + user.company.name
    }

    private fun editUser() {
        val intent = Intent(activity.context, UpdateProfile::class.java)
        startActivity(intent)
    }

    private fun logout() {
        val intent = Intent(activity.context, Login::class.java)
        startActivity(intent)
        activity.requireParentFragment().requireActivity().finish()
    }
}