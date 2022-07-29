<template>
    <div id="dashboard">

        <v-container max-width="1080" fluid>

            <v-row class="mx-5">
                <v-col cols="12">
                    <v-row wrap>
                        <v-col cols="6">
                            <h1 class="subtitle-1 grey--text">Welcome Dashboard</h1>
                        </v-col>
                    </v-row>


                    <v-row wrap>
                        <v-col cols="12" sm="4" md="3">
                            <v-card class="purple">
                                <v-card-title>
                                    <h1 class="subtitle-1 white--text">Category</h1>

                                    <v-row class="justify-end">
                                        <v-icon large class="white--text">mdi-clipboard-list</v-icon>
                                    </v-row>

                                </v-card-title>

                                <v-card-text>
                                    <h1 class="headline white--text">5</h1>

                                    <v-divider></v-divider>

                                    <v-row class="justify-center">
                                        <v-btn small class="mt-5 purple"><v-icon> mdi-forwardburger</v-icon> G0</v-btn>
                                    </v-row>

                                </v-card-text>

                            </v-card>
                        </v-col>

                        <v-col cols="12" sm="4" md="3">
                            <v-card class="primary">
                                <v-card-title>
                                    Sub Category

                                    <v-row class="justify-end">
                                        <v-icon large>mdi-view-list</v-icon>
                                    </v-row>
                                </v-card-title>

                                <v-card-text>
                                    <h1 class="headline">10</h1>

                                    <v-divider></v-divider>

                                    <v-row class="justify-center">
                                        <v-btn small class="mt-5 primary"><v-icon> mdi-forwardburger</v-icon> G0</v-btn>
                                    </v-row>

                                </v-card-text>
                            </v-card>
                        </v-col>

                        <v-col cols="12" sm="4" md="3">
                            <v-card class="info">
                                <v-card-title>
                                    Brand

                                    <v-row class="justify-end">
                                        <v-icon large>mdi-emby</v-icon>
                                    </v-row>
                                </v-card-title>

                                <v-card-text>
                                    <h1 class="headline">30</h1>

                                    <v-divider></v-divider>

                                    <v-row class="justify-center">
                                        <v-btn small class="mt-5 info"><v-icon> mdi-forwardburger</v-icon> G0</v-btn>
                                    </v-row>

                                </v-card-text>
                            </v-card>
                        </v-col>

                        <v-col cols="12" sm="4" md="3">
                            <v-card class="error">
                                <v-card-title>
                                    Tag

                                    <v-row class="justify-end">
                                        <v-icon large>mdi-tag</v-icon>
                                    </v-row>
                                </v-card-title>

                                <v-card-text>
                                    <h1 class="headline">40</h1>

                                    <v-divider></v-divider>

                                    <v-row class="justify-center">
                                        <v-btn small class="mt-5 error"><v-icon> mdi-forwardburger</v-icon> G0</v-btn>
                                    </v-row>

                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>


            </v-row>

        </v-container>

    </div>
</template>

<script>
    export default {
        name: "MyDashboard",
        data () {
            return {

            }
        },
    }
</script>

<style scoped>

</style>