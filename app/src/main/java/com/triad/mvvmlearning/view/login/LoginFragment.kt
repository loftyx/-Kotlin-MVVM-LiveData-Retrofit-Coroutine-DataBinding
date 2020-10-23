package com.triad.mvvmlearning.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.triad.mvvmlearning.R
import com.triad.mvvmlearning.databinding.FragmentLoginBinding
import com.triad.mvvmlearning.network.AuthApi
import com.triad.mvvmlearning.network.Resource
import com.triad.mvvmlearning.repository.LoginRepository
import com.triad.mvvmlearning.responses.loginresponse.LoginResponse
import com.triad.mvvmlearning.utility.Constants
import com.triad.mvvmlearning.utility.PreferenceConfigration
import com.triad.mvvmlearning.utility.UtilityMethods
import com.triad.mvvmlearning.view.BaseFragment
import com.triad.mvvmlearning.view.dashbord.MainActivity


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, LoginRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Success -> {

                    binding.viewmodel = it.value


                    SavePreference(it.value);

                    Toast.makeText( requireContext(),"Logged In", Toast.LENGTH_SHORT).show()

                }

                is Resource.Failure -> {

                    Toast.makeText(requireContext(),"Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

        })

        binding.login.setOnClickListener {


                val paramObject = HashMap<String,String>()
                paramObject.put("username", binding.username.text.toString())
                paramObject.put("password", binding.password.text.toString())
                paramObject.put("login_type", "login")
                paramObject.put("role", "am")
                paramObject.put("device", "Device")

                viewModel.login(paramObject)

        }

    }

    private fun SavePreference(value: LoginResponse) {

        val username: String = value.data.username
        val fname: String = value.data.name
        val uniquecode: String = value.data.uniquecode


        UtilityMethods.setTokenValue(value.data.token )

        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.USER_ID,
            username
        )
        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.USER_PASSWORD,
            binding.password.text.toString()
        )
        PreferenceConfigration.setPreference(Constants.PreferenceConstants.USER_NAME, username)
        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.STATUS,
            value.data.status
        )
        PreferenceConfigration.setPreference(Constants.PreferenceConstants.FIRST_NAME, fname)
        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.Role,
           "am"
        )

        PreferenceConfigration.setPreference(Constants.PreferenceConstants.UNIQUE_CODE, uniquecode)
        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.ChangePasswordRequired,
            "1"
        )

        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.ADDRESS_ONE,
            value.data.address
        )

        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.CITY,
            value.data.city
        )

        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.MOBILE,
            value.data.mobile
        )
        PreferenceConfigration.setPreference(
            Constants.PreferenceConstants.NOTI_COUNT,
            value.data.noti_count
        )



        val loginIntent = Intent(
            getActivity(),
            MainActivity::class.java
        )
        loginIntent.putExtra("from", "login")
        startActivity(loginIntent)
        val activity = getActivity() as Activity
        activity.finish()
    }







    override fun getViewModel() = LoginViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?) : ViewDataBinding {

      return  DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

    }


    override fun getFragmentRepository(): LoginRepository {

        return  LoginRepository(remoteDataSource.buildApi(AuthApi::class.java))

    }

}