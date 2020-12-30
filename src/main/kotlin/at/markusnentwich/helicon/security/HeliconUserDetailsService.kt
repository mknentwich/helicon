package at.markusnentwich.helicon.security

import at.markusnentwich.helicon.repositories.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class HeliconUserDetailsService(
    @Autowired val accountRepository: AccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("username was null")
        }
        val opt = accountRepository.findById(username)
        // TODO use lambdas
        if (opt.isPresent) {
            return toUser(opt.get())
        } else {
            throw UsernameNotFoundException("no user with name '$username' found")
        }
    }
}
