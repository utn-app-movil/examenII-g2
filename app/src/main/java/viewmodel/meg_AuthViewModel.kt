package cr.ac.utn.appmovil.rooms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.rooms.model.MegUser
import com.utn.rooms.repository.meg_Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class meg_AuthViewModel : ViewModel() {
    private val repo = meg_Repository()

    private val _user = MutableLiveData<MegUser?>()
    val user: LiveData<MegUser?> = _user

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun login(username: String, password: String) {
        scope.launch {
            runCatching { repo.auth(username, password) }
                .onSuccess { resp ->
                    _message.postValue(resp.message)
                    if (resp.responseCode == "INFO_FOUND" && resp.data != null) {
                        _user.postValue(resp.data)
                    } else {
                        _user.postValue(null)
                    }
                }
                .onFailure { e ->
                    _message.postValue("Network error: ${e.localizedMessage}")
                    _user.postValue(null)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
