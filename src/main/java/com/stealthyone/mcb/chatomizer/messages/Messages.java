/*
 * Chatomizer - Advanced chat plugin with endless possibilities
 * Copyright (C) 2014 Stealth2800 <stealth2800@stealthyone.com>
 * Website: <http://stealthyone.com/bukkit>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.stealthyone.mcb.chatomizer.messages;

public class Messages {

    private Messages() { }

    public static class ErrorMessages {

        private final static String CATEGORY = "errors";

        private ErrorMessages() { }

        public final static ChatomizerMessageRef FORMAT_ALREADY_SET = new ChatomizerMessageRef(CATEGORY, "format_already_set");
        public final static ChatomizerMessageRef FORMAT_DEFAULT_ALREADY_SET = new ChatomizerMessageRef(CATEGORY, "format_default_already_set");
        public final static ChatomizerMessageRef FORMAT_NOT_FOUND = new ChatomizerMessageRef(CATEGORY, "format_not_found");
        public final static ChatomizerMessageRef NO_PERMISSION = new ChatomizerMessageRef(CATEGORY, "no_permission");
        public final static ChatomizerMessageRef NO_PERMISSION_FORMAT = new ChatomizerMessageRef(CATEGORY, "no_permission_format");
        public final static ChatomizerMessageRef RELOAD_ERROR = new ChatomizerMessageRef(CATEGORY, "reload_error");
        public final static ChatomizerMessageRef SAVE_ERROR = new ChatomizerMessageRef(CATEGORY, "save_error");
        public final static ChatomizerMessageRef UNABLE_TO_CHAT = new ChatomizerMessageRef(CATEGORY, "unable_to_chat");
        public final static ChatomizerMessageRef UNABLE_TO_GET_CHATTER = new ChatomizerMessageRef(CATEGORY, "unable_to_get_chatter");
        public final static ChatomizerMessageRef UNKNOWN_COMMAND = new ChatomizerMessageRef(CATEGORY, "unknown_command");

    }

    public static class NoticeMessages {

        private final static String CATEGORY = "notices";

        private NoticeMessages() { }

        public final static ChatomizerMessageRef FORMAT_DEFAULT_NOTICE = new ChatomizerMessageRef(CATEGORY, "format_default_notice");
        public final static ChatomizerMessageRef FORMAT_DEFAULT_SET = new ChatomizerMessageRef(CATEGORY, "format_default_set");
        public final static ChatomizerMessageRef FORMAT_SET = new ChatomizerMessageRef(CATEGORY, "format_set");
        public final static ChatomizerMessageRef PLUGIN_RELOADED = new ChatomizerMessageRef(CATEGORY, "plugin_reloaded");
        public final static ChatomizerMessageRef PLUGIN_SAVED = new ChatomizerMessageRef(CATEGORY, "plugin_saved");

    }

    public static class PluginMessages {

        private final static String CATEGORY = "plugin";

        private PluginMessages() { }

        public final static ChatomizerMessageRef FORMATS_LIST = new ChatomizerMessageRef(CATEGORY, "formats_list");
        public final static ChatomizerMessageRef FORMATS_LIST_NONE = new ChatomizerMessageRef(CATEGORY, "formats_list_none");

    }

    public static class UsageMessages {

        private final static String CATEGORY = "usages";

        private UsageMessages() { }

        public final static ChatomizerMessageRef CHATOMIZER_CHAT = new ChatomizerMessageRef(CATEGORY, "chatomizer_chat");
        public final static ChatomizerMessageRef CHATOMIZER_CHAT_DIRECT = new ChatomizerMessageRef(CATEGORY, "chatomizer_chat_direct");
        public final static ChatomizerMessageRef CHATOMIZER_FORMAT = new ChatomizerMessageRef(CATEGORY, "chatomizer_format");
        public final static ChatomizerMessageRef CHATOMIZER_FORMAT_DIRECT = new ChatomizerMessageRef(CATEGORY, "chatomizer_format_direct");
        public final static ChatomizerMessageRef CHATOMIZER_FORMAT_INFO = new ChatomizerMessageRef(CATEGORY, "chatomizer_format_info");
        public final static ChatomizerMessageRef CHATOMIZER_HELP = new ChatomizerMessageRef(CATEGORY, "chatomizer_help");

    }

}