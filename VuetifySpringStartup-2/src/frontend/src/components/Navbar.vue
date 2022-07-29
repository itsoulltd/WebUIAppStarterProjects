<template>
    <nav>
        <v-app-bar app flat color="white">

            <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

            <v-toolbar-title>NDL</v-toolbar-title>

            <v-autocomplete
                    v-model="select"
                    :loading="loading"
                    :itemsss="items"
                    :search-input.sync="search"
                    cache-items
                    class="mx-4 ml-15"
                    flat
                    hide-no-data
                    hide-details
                    label="Search Here..."
                    solo-inverted
            ></v-autocomplete>

            <v-spacer></v-spacer>

            <v-btn
                    icon
            >
                <v-icon>mdi-bell</v-icon>
            </v-btn>


            <v-menu offset-y
                    left
                    bottom
            >
                <template v-slot:activator="{ on, attrs }">
                    <v-btn
                            icon
                            v-bind="attrs"
                            v-on="on"
                    >
                        <v-icon>mdi-dots-vertical</v-icon>
                    </v-btn>
                </template>

                <v-list>
                    <v-list-item
                            v-for="(item, index) in ite"
                            :key="index"
                    >

                        <v-icon v-text="item.icon"> </v-icon>
                        <v-list-item-title style="cursor: pointer">{{ item.title }}</v-list-item-title>

                    </v-list-item>
                </v-list>
            </v-menu>
        </v-app-bar>

        <v-navigation-drawer
                app
                absolute
                left
                v-model="drawer"
                :permanent="$vuetify.breakpoint.mdAndUp"
                style="z-index: 10"
                src="https://i.picsum.photos/id/634/1920/1080.jpg?hmac=qYsuyzEm2Neem1QJnkdC5EI7RtnJ7q_aThq8vZht25s"
        >

            <v-list-item class="px-2" style="margin-left: 10px;margin-top: 0px;">
                <v-list-item-avatar>
                    <v-img src="https://randomuser.me/api/portraits/men/85.jpg"></v-img>
                </v-list-item-avatar>

                <v-list-item-title style="margin-left: 10px">John Leider</v-list-item-title>
            </v-list-item>

            <v-divider></v-divider>

            <v-list nav dense>

                <v-list-item-group
                        v-model="selectedItem"
                        color="primary"
                >
                    <v-list-item
                            v-for="(item, i) in items"
                            :key="i"
                    >
                        <v-list-item-icon>
                            <v-icon v-text="item.icon"></v-icon>
                        </v-list-item-icon>

                        <v-list-item-content>
                            <v-list-item-title v-text="item.text"></v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </v-list-item-group>

                <v-list-group
                        v-for="item in itemss"
                        :key="item.title"
                        v-model="item.active"
                        :prepend-icon="item.action"
                        no-action
                >
                    <template v-slot:activator>
                        <v-list-item-content>
                            <v-list-item-title v-text="item.title"></v-list-item-title>
                        </v-list-item-content>
                    </template>

                    <v-list-item
                            v-for="child in item.items"
                            :key="child.title"
                    >
                        <v-list-item-content>
                            <v-list-item-title v-text="child.title"></v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </v-list-group>
            </v-list>


        </v-navigation-drawer>


    </nav>
</template>

<script>
    export default {
        name: "MyNavbar",
        data () {
            return {
                drawer: null,
                selectedItem: 0,
                items: [
                    { text: 'Dashboard', icon: 'mdi-view-dashboard' },
                    { text: 'User', icon: 'mdi-account-multiple' },
                ],

                itemss: [
                    {
                        action: 'mdi-ticket',
                        items: [{ title: 'List Item' }],
                        title: 'Attractions',
                    }
                ],
                ite: [
                    { title: 'Profile', icon: 'mdi-face-man-profile'},
                    { title: 'Settings', icon: 'mdi-cog' },
                    { title: 'Logout', icon: 'mdi-logout' },
                ],

                loading: false,
                itemsss: [],
                search: null,
                select: null,
                states: [
                    'Alabama',
                    'Alaska',
                    'American Samoa',
                    'Arizona',
                    'Arkansas',
                    'California',
                    'Colorado',
                    'Connecticut',
                    'Delaware',
                    'District of Columbia',
                    'Federated States of Micronesia',
                    'Florida',
                    'Georgia',
                    'Guam',
                    'Hawaii',
                    'Idaho',
                    'Illinois',
                    'Indiana',
                    'Iowa',
                    'Kansas',
                    'Kentucky',
                    'Louisiana',
                    'Maine',
                    'Marshall Islands',
                    'Maryland',
                    'Massachusetts',
                    'Michigan',
                    'Minnesota',
                    'Mississippi',
                    'Missouri',
                    'Montana',
                    'Nebraska',
                    'Nevada',
                    'New Hampshire',
                    'New Jersey',
                    'New Mexico',
                    'New York',
                    'North Carolina',
                    'North Dakota',
                    'Northern Mariana Islands',
                    'Ohio',
                    'Oklahoma',
                    'Oregon',
                    'Palau',
                    'Pennsylvania',
                    'Puerto Rico',
                    'Rhode Island',
                    'South Carolina',
                    'South Dakota',
                    'Tennessee',
                    'Texas',
                    'Utah',
                    'Vermont',
                    'Virgin Island',
                    'Virginia',
                    'Washington',
                    'West Virginia',
                    'Wisconsin',
                    'Wyoming',
                ],
            }
        },
    }
</script>

<style scoped>

</style>