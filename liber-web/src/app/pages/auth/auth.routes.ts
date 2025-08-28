import { Routes } from '@angular/router'
import { Access } from './access'
import { Error } from './error'
import { Login } from './login'
import { SignUp } from './signup'

export default [
    { path: 'access', component: Access },
    { path: 'error', component: Error },
    { path: 'login', component: Login },
    { path: 'signup', component: SignUp },
] as Routes
