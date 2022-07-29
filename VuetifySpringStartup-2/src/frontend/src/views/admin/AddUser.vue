<template>
    <div id="add_user">
        <v-row class="mx-5">

            <v-col cols="12">
                <v-row wrap>
                    <v-col cols="6">
                        <h1 class="subtitle-1 grey--text mt-5">Add User</h1>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12">
                        <v-card>
                            <v-card-title>Add User</v-card-title>

                            <v-divider></v-divider>

                            <v-card-text>
                                <v-form v-on:submit.prevent="addBrand">

                                    <v-text-field type="text" :rules="nameRules" :counter="10" required  label="Name"></v-text-field>
                                    <v-text-field type="email" :rules="emailRules" required label="Email"></v-text-field>
                                    <v-text-field type="text" label="Phone"></v-text-field>
                                    <v-text-field type="text" label="Country"></v-text-field>
                                    <v-text-field type="text" label="City"></v-text-field>

                                    <v-row class="justify-end mt-5">
                                        <v-btn text  class="info mr-2" router to="/dashboard/user">Back</v-btn>
                                        <v-btn text type="submit" class="success mr-2">Submit</v-btn>
                                    </v-row>
                                </v-form>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </v-row>
            </v-col>

        </v-row>
    </div>
</template>

<script>
    export default {
        name: "MyAddUser",
        data: () => ({
            valid: false,
            name: '',
            nameRules: [
                v => !!v || 'Name is required',
                v => v.length <= 10 || 'Name must be less than 10 characters',
            ],
            email: '',
            emailRules: [
                v => !!v || 'E-mail is required',
                v => /.+@.+/.test(v) || 'E-mail must be valid',
            ],
        }),
    }
</script>

<style scoped>

</style>