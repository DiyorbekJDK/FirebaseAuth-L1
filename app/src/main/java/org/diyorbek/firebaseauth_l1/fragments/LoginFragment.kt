package org.diyorbek.firebaseauth_l1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import org.diyorbek.firebaseauth_l1.R
import org.diyorbek.firebaseauth_l1.databinding.FragmentFirstBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val auth by lazy {FirebaseAuth.getInstance()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            binding.progressBar.isVisible = true
            binding.button.isEnabled = false
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    binding.progressBar.isVisible = false
                    binding.button.isEnabled = true
                    if (it.isSuccessful){
                        Toast.makeText(
                            requireContext(),
                            "Successfully logged in",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_FirstFragment_to_homeFragment)
                    }else{
                        Toast.makeText(requireContext(), it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.textView.setOnClickListener{
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}