<template>
    <div id="user">
        <v-row class="mx-5">

            <v-col cols="12">
                <v-row wrap>
                    <v-col cols="6">
                        <h1 class="subtitle-1 grey--text mt-5">Welcome Users</h1>
                    </v-col>
                </v-row>

                <v-row wrap>
                    <v-col cols="12">
                        <v-card>

                            <v-row>
                                <v-col col="6">
                                    <v-card-title>Users List</v-card-title>
                                </v-col>

                                <v-col cols="6">
                                    <v-card-actions class="justify-end">
                                        <v-btn text color="success" router to="/dashboard/add_user">
                                            <v-icon small left>mdi-plus</v-icon>
                                            <span>Add New</span>
                                        </v-btn>
                                    </v-card-actions>
                                </v-col>
                            </v-row>

                            <v-divider></v-divider>

                            <v-card-text>

                                <v-card-title>
                                    All Brand List
                                    <v-spacer></v-spacer>

                                    <v-text-field
                                            v-model="search"
                                            append-icon="mdi-magnify"
                                            label="Search"
                                            single-line
                                            hide-details
                                            class="mb-5"
                                    ></v-text-field>
                                </v-card-title>

                                <v-data-table :headers="headers" :items="users" :search="search" dense  class="elevation-1">


                                    <template v-slot:[`item.actions`]="{ item }">

                                        <v-tooltip top>
                                            <template v-slot:activator="{ on }">
                                                <v-btn small text color="grey" v-on="on" router :to="`/dashboard/edit-brand/${item.id}`">
                                                    <v-icon left small>mdi-pencil</v-icon>
                                                    <span right class="caption text-lowercase">Edit</span>
                                                </v-btn>
                                            </template>
                                            <span>Edit</span>
                                        </v-tooltip>

                                        <v-tooltip top>
                                            <template v-slot:activator="{ on }">
                                                <v-btn small text color="grey" v-on="on" @click="deleteBrand(item.id)">
                                                    <v-icon left small>mdi-delete</v-icon>
                                                    <span right class="caption text-lowercase">Delete</span>
                                                </v-btn>
                                            </template>
                                            <span>Delete</span>
                                        </v-tooltip>

                                    </template>

                                </v-data-table>

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
        name: "MyUser",
        data(){
            return{
                search: '',
                users: [
                    {
                        id: 1,
                        name: 'pavel',
                        email: 'aminur@gmail.com',
                        phone: '01772119941',
                        country: 'Bangladesh',
                        city: 'Dhaka'
                    },
                    {
                        id: 2,
                        name: 'pavel',
                        email: 'aminur@gmail.com',
                        phone: '01772119941',
                        country: 'Bangladesh',
                        city: 'Dhaka',
                    }
                ]
            }
        },

        computed:{
            headers(){
                return [
                    {text: '#Sl', value:'id',  align: 'start', sortable: false},
                    {text: 'Name', value: 'name'},
                    {text: 'Email', value:'email'},
                    {text: 'Phone', value:'phone'},
                    {text: 'Country', value:'country'},
                    {text: 'City', value:'city'},
                    {text: 'Actions', value: 'actions', align: 'center', sortable: false },
                ]
            },
        },
    }
</script>

<style scoped>

</style>