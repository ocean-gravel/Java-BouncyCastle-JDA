package com.example.advancedcomputernetworksca2.DiscordJDA;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

public class Embeds extends ListenerAdapter {

    // Admin
    EmbedBuilder adminEmbed = new EmbedBuilder();
    Button createProfileButton = Button.primary("create-profile","Create Profile");
    Button deleteProfileButton = Button.danger("delete-profile","Delete Profile");
    Button checkAllUsersProfileButton = Button.secondary("check-all","Check Users");

    // User
    EmbedBuilder userEmbed = new EmbedBuilder();
    Button checkProfileButton = Button.primary("check-profile","Check Profile");
    Button sendMessageButton = Button.primary("send-msg","Send Message");
    Button decryptButton = Button.primary("decrypt-msg","Decrypt Msg");
    long guildId = 1; // Replace YOUR_GUILD_ID with the actual guild ID


    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        /*
            I want there to be two embeds
            One for ADMIN
                Buttons to include in admin embed:
                [Create] Profile for X + give everyone new profile details apart from their names
                [Delete] Profile for X + give everyone new profile details apart from their names
                [Check the users] who have a profile :)


            One for REGULAR USES (Which can also be used by the admin)
            [Check] profile + cert of user
            [Send] an encrypted message at them
            [Decrypt] a message from someone using your private key :3

         */
        Message msg = event.getMessage();
        String[] message = event.getMessage().getContentRaw().split(" ");

        String command = message[0];
        String person = message[1];
        if(msg.getContentRaw().equalsIgnoreCase("$admin")){}
        /* else if the command is USER B) */
        else if(message.length == 2 && message[0].equalsIgnoreCase("$user")){
            Guild guild = event.getJDA().getGuildById(guildId);
            boolean isInGuild = false;
            if (guild != null) {
                for (Member member : event.getGuild().getMembers()) {
                    if (person.equals(member.toString())) {isInGuild = true;}
                    if (isInGuild) {break;}
                }
                if (isInGuild) {
                    // create the embed?
                    userEmbed.setTitle(person + "'s Information");
                }
            }

        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event){}
}
