package com.trustledger.adminaitrust.fragments.create_user

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trustledger.adminaitrust.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.firebase.Timestamp
import com.trustledger.adminaitrust.Utils
import com.trustledger.adminaitrust.ViewModel.UserViewModel
import com.trustledger.adminaitrust.databinding.FragmentCreateUserBinding
import com.trustledger.adminaitrust.models.UserModel
import com.trustledger.adminaitrust.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateUserFragment : Fragment() {
    private var _binding: FragmentCreateUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private  lateinit var utils:Utils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        utils = Utils(requireContext())
        binding.depositButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val phoneNumber = binding.phoneNoEditText.text.toString().trim()
            val referralCode = binding.referralCodeEditText.text.toString().trim()

            // Validation
            if (firstName.isEmpty() || !firstName.matches("[a-zA-Z]+".toRegex())) {
                binding.firstNameEditText.error = "Enter a valid first name"
                return@setOnClickListener
            }

            if (lastName.isEmpty() || !lastName.matches("[a-zA-Z]+".toRegex())) {
                binding.lastNameEditText.error = "Enter a valid last name"
                return@setOnClickListener
            }

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (email.isEmpty() || !email.matches(emailPattern.toRegex())) {
                binding.emailEditText.error = "Enter a valid email"
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                binding.passwordEditText.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{11}".toRegex())) {
                binding.phoneNoEditText.error = "Enter a valid 11-digit phone number"
                return@setOnClickListener
            }

            if (referralCode.isNotEmpty() && !referralCode.matches("[a-zA-Z0-9]+".toRegex())) {
                binding.referralCodeEditText.error = "Referral code must be alphanumeric"
                return@setOnClickListener
            }

            // All validations passed
            val userModel = UserModel(
                uid = "",
                docId = "",
                name = firstName,
                lastName = lastName,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                referralCode = referralCode,
                deviceToken = "",
                createdAt = Timestamp.now(),
                isBlocked = false,
                status = "inactive",
                createdByAdmin = true
            )

            // Launch coroutine to register user
            utils.startLoadingAnimation()
            lifecycleScope.launch(Dispatchers.IO) {
                userViewModel.registerUser(userModel) { isUserRegistered, uid ->
                    if (isUserRegistered) {
                        utils.endLoadingAnimation()
                        val bundle = Bundle().apply { putString("user_email", email) }
                        view.findNavController().navigate(
                            R.id.action_createUserFragment_to_depositFragment, bundle
                        )
                    } else {
                        utils.endLoadingAnimation()
                    }
                }
            }
        }

        binding.togglePasswordVisibility.setOnClickListener {
            if (binding.passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // If password is hidden, show it
                binding.passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_24) // Replace with your show icon
            } else {
                // If password is visible, hide it
                binding.passwordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_off_24) // Replace with your hide icon
            }
            binding.passwordEditText.setSelection(binding.passwordEditText.text.length) // Maintain cursor at the end
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
