<template>
    <div id="login" style="display: flex;justify-content: center;align-items: center;margin-top: 200px">
        <v-col cols="10" lg="4" class="mx-auto">
            <v-card class="pa-4">
                <div class="text-center">
                    <v-avatar size="100" class="indigo lighten-4">
                        <v-icon size="40" color="indigo">mdi-account</v-icon>
                    </v-avatar>
                    <h2 class="indigo--text">Login Now</h2>
                </div>
                <v-form v-on:submit.prevent="login">
                    <v-card-text>
                        <v-text-field
                                v-model="email"
                                :rules="emailRules"
                            type="email"
                            label="Email"
                            placeholder="Email"
                            prepend-inner-icon="mdi-account"
                        />

                        <v-text-field
                                v-model="password"
                                :rules="passwordRules"
                                :type="passwordShow ? 'text' : 'password'"
                                label="Password"
                                placeholder="Password"
                                prepend-inner-icon="mdi-key"
                                :append-icon="passwordShow ? 'mdi-eye' : 'mdi-eye-off'"
                                @click:append="passwordShow = !passwordShow"
                        />

                        <v-switch label="Remember Me" color="indigo"></v-switch>
                    </v-card-text>

                    <v-card-actions class="justify-center">
                        <v-btn type="submit" color="indigo">
                            <span class="white--text px-8">Login</span>
                        </v-btn>
                    </v-card-actions>
                </v-form>
            </v-card>
        </v-col>
    </div>
</template>

<script>
    export default {
        name: "MyLogin",
        data(){
            return{
                passwordShow: false,
                password: '',
                passwordRules: [
                    v => !!v || 'Password is required',
                    v => v.length >= 6 || 'Password must be 6 characters or more',
                ],
                email: '',
                emailRules: [
                    v => !!v || 'E-mail is required',
                    v => /.+@.+/.test(v) || 'E-mail must be valid',
                ],
            }
        },

        methods:{
            login: function(){

                this.$router.push('/dashboard');
            }
        }
    }
</script>

<style scoped>

</style>