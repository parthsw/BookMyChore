package com.book.chore.utils

object ChoreConstants {
    class Collections {
        companion object {
            const val CHORE_USERS = "users"
            const val CHORE_SERVICES = "services"
            const val TASK_DOERS = "task_doers"
        }
    }

    class Parameters {
        companion object {
            const val userEmail = "userEmail"
        }
    }

    class PrefNames {
        companion object {
            const val PREF_NAME_USER = "user_prefs"
        }
    }

    class PrefKeys {
        companion object {
            private const val PREFIX_PREF_KEY = "pref_key_"
            const val PREF_KEY_LOGGED_IN = PREFIX_PREF_KEY + "logged_in"
            const val PREF_KEY_LOGGED_IN_USER_ID = PREFIX_PREF_KEY + "user_id"
            const val PREF_KEY_SKIPPED_LOGIN = PREFIX_PREF_KEY + "skipped_login"
        }
    }

    class Images {
        companion object {
            const val CAPTURE_IMAGE = 0
            const val CHOOSE_FROM_GALLERY = 1
        }
    }

    class AppConstant {
        companion object {
            const val SERVICE_CITY = "service_city"
            const val LOCATION = "location "
        }
    }

    class AlertConstant{
        companion object {
            const val SERVICE_SELECT_LOCATION_TITLE = "Location is not selected"
            const val SERVICE_SELECT_LOCATION_MESSAGE = "Please enter your location to see the available services."
            const val OKAY_BUTTON = "OK"
            const val PROCEED = "OK"


        }
    }

}
