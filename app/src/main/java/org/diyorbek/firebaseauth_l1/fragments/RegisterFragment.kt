package org.diyorbek.firebaseauth_l1.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import org.diyorbek.firebaseauth_l1.R
import org.diyorbek.firebaseauth_l1.databinding.FragmentSecondBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val fireStore by lazy { FirebaseFirestore.getInstance() } // saves user to fireStore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        binding.button.setOnClickListener {
            binding.progressBar.isVisible = true
            binding.button.isEnabled = false

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotBlank()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        binding.progressBar.isVisible = false
                        binding.button.isEnabled = true
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Successfully registered",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_SecondFragment_to_homeFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

        }
        binding.textView.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.button2.setOnClickListener {
            saveToFireStore(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim(),
                binding.name.text.toString().trim()
            )
        }

    }

    private fun saveToFireStore(email: String, password: String, fullName: String) {
        fireStore.collection("users")
            .add(org.diyorbek.firebaseauth_l1.model.User2(email, password, fullName))
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d("@@@", "saveToFireStore: ${it.message.toString()}")
            }
            .addOnSuccessListener {
                println("success")
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}