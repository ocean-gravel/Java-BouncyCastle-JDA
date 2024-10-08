package com.example.advancedcomputernetworksca2.DiscordJDA;

// JDA Imports
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

// Java Standard Imports
import java.awt.*;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


// Bouncy Castle
import com.example.advancedcomputernetworksca2.BouncyCastle.Profile;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import static com.example.advancedcomputernetworksca2.BouncyCastle.CertificateGenerator.generateSelfSignedCertificate;
import static  com.example.advancedcomputernetworksca2.BouncyCastle.encryptDecrypt.decrypt;
import static  com.example.advancedcomputernetworksca2.BouncyCastle.encryptDecrypt.encrypt;
import static com.example.advancedcomputernetworksca2.BouncyCastle.keyCreator.generateRSAKeyPair;


public class Embeds extends ListenerAdapter {

    // Admin
    EmbedBuilder adminEmbed = new EmbedBuilder();
    Button createProfileButton = Button.primary("create-profile","Create Profile");
    Button deleteProfileButton = Button.danger("delete-profile","Delete Profile");

    Button checkAllUsersProfileButton = Button.secondary("check-all","Check Users");

    EmbedBuilder createProfileEmbed = new EmbedBuilder();
    Button createProfileForUserButton = Button.primary("create-prf-user","Create");
    Button nextPersonButton = Button.primary("next-person","Next");
    Button previousPersonButton = Button.primary("previous-person","Previous");

    // I will make something similar for the delete feature.
    EmbedBuilder deleteProfileEmbed = new EmbedBuilder();
    Button deleteProfileForUserButton = Button.primary("delete-prf-user","Delete");
    Button nextPersonDeleteButton = Button.primary("next-person-del","Next");
    Button previousPersonDeleteButton = Button.primary("previous-person-del","Previous");


    Map<String, Profile> userProfiles = new HashMap<>();

    /*----------------------------------------------------------------------------------------------------------------*/

    // User
    EmbedBuilder userEmbed = new EmbedBuilder();
    EmbedBuilder certificateProfileEmbed = new EmbedBuilder();
    Button checkProfileButton = Button.primary("check-profile","Check Profile");
    Button sendMessageButton = Button.primary("send-msg","Send Message");
    Button decryptButton = Button.primary("decrypt-msg","Decrypt Msg");

    long adminEmbedMessageId, userEmbedMessageId;
    int nonBotUserIndex = 0;
    List<User> nonBotUsers = new ArrayList<>();
    List<String> usersWithProfiles = new ArrayList<>();

    KeyPair keyPair;

    String nameFromCommand;



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

        // Get the list of members from the server
        nonBotUsers.clear(); // If the admin were to type in $admin multiple times then the list of the nonBot users
        // would duplicate on the embed if I didn't have this line.
        List<Member> members = event.getGuild().getMembers();
        System.out.println("The list of the members in the Server: \n" + Arrays.toString(members.toArray()));

        StringBuilder usersList = new StringBuilder();
        // Iterate through the list of members and extract their User objects if they're not bots
        for (Member member : members) {
            // Check if the member is a bot
            if (!member.getUser().isBot()) {
                // If the member is not a bot, add them to the list of non-bot users
                nonBotUsers.add(member.getUser());
                System.out.println(member.getUser().getEffectiveName());
                System.out.println("The list of members who are not bots are: \n"
                        + Arrays.toString(nonBotUsers.toArray()));
            }
        }
        for (User user : nonBotUsers) {
            usersList.append(user.getName()).append(" - ");
            usersList.append(user.getEffectiveName()).append("\n");
        }

        Date date = new Date();
        Role adminRole = event.getGuild().getRolesByName("bossMan", true).stream().findFirst().orElse(null);
        String[] message = event.getMessage().getContentRaw().split(" ");

        if (message.length == 1 && message[0].equalsIgnoreCase("$admin")) {
            if (adminRole != null && Objects.requireNonNull(event.getMember()).getRoles().contains(adminRole)) {
                System.out.println("BEANS");
                adminEmbed.setTitle("ADMIN PANEL");


                // We want to get the Message ID of this embed, so that we can "edit it" and change to other embeds
                // whenever we press different buttons.

                adminEmbed.setDescription("List of Users:\n" + usersList);
                adminEmbed.clearFields();
                event.getChannel().sendMessageEmbeds(adminEmbed.build())
                        .setActionRow(createProfileButton, deleteProfileButton, nextPersonButton, previousPersonButton) // Add the button here
                        .queue( messageEmbed -> {
                            adminEmbedMessageId = messageEmbed.getIdLong(); // Gets the ID of the message that was just sent
                            System.out.println("Message ID: " + adminEmbedMessageId);
                        });


                createProfileEmbed.setColor(Color.green);
                createProfileEmbed.setTitle("Create profile for: " + nonBotUsers.get(nonBotUserIndex).getName());
                createProfileEmbed.setFooter("Request was made @ " + date, event.getGuild().getIconUrl());

            } else {
                event.getChannel().sendMessage("You cannot use this command as a non-admin").queue();
            }
        }
        // else if not an admin
             if (message.length == 2 && message[0].equalsIgnoreCase("$user")) {
                String userName = message[1];
                nameFromCommand = userName;
                // here we can either use getMembersByEffectiveName or getMembersByName();
                // either works

                members = event.getGuild().getMembersByEffectiveName(userName, true);

                if (members.isEmpty()) {
                    event.getChannel().sendMessage("User not found.").queue();
                    return;
                }
                //Member member = members.get(0);
                User user = members.get(0).getUser();
                // This embed is going to serve as the first page :)
                String avatar = user.getAvatarUrl();
                userEmbed.clearFields();
                userEmbed.setTitle(user.getEffectiveName() + "'s Information");
                userEmbed.setColor(0x8f00ff);
                userEmbed.addField("ID", user.getAsTag(), true);
                userEmbed.addField("Server Name", user.getEffectiveName(), false);
                userEmbed.addField("Avatar", "The Avatar is below, " + Objects.requireNonNull(event.getMember()).getAsMention(), true);
                userEmbed.setImage(avatar);
                userEmbed.setFooter("Request was made @ " + date, event.getGuild().getIconUrl());
                // Sending the embed with a button
                event.getChannel().sendMessageEmbeds(userEmbed.build())
                        .setActionRow(checkProfileButton, sendMessageButton, decryptButton) // Add the button here
                        .queue(
                                messageEmbed -> {
                                    userEmbedMessageId = messageEmbed.getIdLong(); // Gets the ID of the message that was just sent
                                    System.out.println("Message ID :): " + userEmbedMessageId);
                                });

                certificateProfileEmbed.setTitle(user.getEffectiveName() + "'s Certificate");
                certificateProfileEmbed.addField("Certificate"," ",true);

            }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event){


        System.out.println("The users with a profile: \n"
                + Arrays.toString(usersWithProfiles.toArray()));
        // User embed buttons
        if(event.getComponentId().equals("check-profile")) {
            String channelId = "1224333555879120977"; // Replace with the actual channel ID
            TextChannel channel = Objects.requireNonNull(event.getGuild()).getTextChannelById(channelId);
            assert channel != null;

            System.out.println("OK OK OK: " + nameFromCommand);


            // this is the line that is messing it up
            System.out.println(nonBotUsers);

            User currentUser = nonBotUsers.get(nonBotUserIndex);
            String userId = currentUser.getId();

            Profile profile = userProfiles.get(userId);

            if (profile != null) {
                X509Certificate certificate = profile.getCertificate();
                // Now use this certificate to fill embed with cert info.
                String subjectName = certificate.getSubjectX500Principal().getName();
                String issuerName = certificate.getIssuerX500Principal().getName();
                String serialNumber = certificate.getSerialNumber().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String validFrom = sdf.format(certificate.getNotBefore());
                String validTo = sdf.format(certificate.getNotAfter());
                PublicKey publicKey = certificate.getPublicKey();

                // Clear previous fields and populate with certificate information
                certificateProfileEmbed.clearFields();
                certificateProfileEmbed.setTitle(currentUser.getName() + "'s Certificate Information");
                certificateProfileEmbed.addField("Subject Name", subjectName, false);
                certificateProfileEmbed.addField("Issuer Name", issuerName, false);
                certificateProfileEmbed.addField("Serial Number", serialNumber, false);
                certificateProfileEmbed.addField("Valid From", validFrom, false);
                certificateProfileEmbed.addField("Valid To", validTo, false);
                certificateProfileEmbed.addField("Public Key: ", publicKey.toString(), false); // You might want to format this
                certificateProfileEmbed.setColor(new Color(0x8f00ff)); // Set a color

                // Edit the message to show the new embed with certificate details
                event.getChannel().editMessageById(userEmbedMessageId, " ")
                        .setEmbeds(certificateProfileEmbed.build())
                        .queue();
            } else {
                event.reply("No profile found for this user.").setEphemeral(true).queue();
            }

        }
        // Admin embed buttons

        Role adminRole = Objects.requireNonNull(event.getGuild()).getRolesByName("bossMan", true)
                .stream().findFirst().orElse(null);
        if (adminRole != null && Objects.requireNonNull(event.getMember()).getRoles().contains(adminRole)) {
            if (event.getComponentId().equals("create-profile")) {

                adminEmbed.clearFields();
                createProfileEmbed.clearFields();
                event.getChannel().editMessageById(adminEmbedMessageId, " ")
                        .setEmbeds(createProfileEmbed.build())
                        .setActionRow(createProfileForUserButton, nextPersonButton, previousPersonButton)
                        .queue();


                System.out.println("BEANS1");
            } else if (event.getComponentId().equals("delete-profile")) {
                System.out.println("BEANS2");
            } else if (event.getComponentId().equals("check-all")) {
                System.out.println("BEANS3");
            } else if (event.getComponentId().equals("create-prf-user")) {
                //im starting to think I should be working off ID, because they are unique?
                User currentUser = nonBotUsers.get(nonBotUserIndex);
                System.out.println("Current User: " + currentUser.getId());
                String userId = currentUser.getId();

                // here a new profile containing the keys + x509 certificate will be made for the current user
                // displayed :)
                try {
                    keyPair = generateRSAKeyPair();
                    X509Certificate certificate = generateSelfSignedCertificate(keyPair);
                    Profile userProfile = new Profile(userId, certificate, keyPair.getPublic());
                    userProfiles.put(userId, userProfile); // Store the profile in the map
                    event.reply("A X.509 Certificate and keys have been created for the user.")
                            .setEphemeral(true).queue();
                } catch (Exception e) {
                    event.reply("Failed to create profile: " + e.getMessage()).setEphemeral(true).queue();
                    e.printStackTrace();
                }
                event.reply("A x509 Certificate has been created for the User along with public and private keys")
                        .setEphemeral(true).queue();
                if (!usersWithProfiles.contains(currentUser.getId())) usersWithProfiles.add(currentUser.getId());

                System.out.println("BEANS3");
            } else if (event.getComponentId().equals("next-person")) {
                // Increment the user index, but ensure it does not exceed the size of the list
                // This will loop back to 0 if it reaches the end, cause of the modulus B)
                nonBotUserIndex = (nonBotUserIndex + 1) % nonBotUsers.size();

                User nextUser = nonBotUsers.get(nonBotUserIndex);

                adminEmbed.clearFields();
                createProfileEmbed.clearFields();
                // Update the createProfileEmbed with the information of the next user
                createProfileEmbed.clear(); // Clear previous settings
                createProfileEmbed.setColor(Color.green); // Set the color again if needed
                createProfileEmbed.setTitle("Create profile for: " + nextUser.getName());
                createProfileEmbed.setFooter("Request was made @ " +
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), event.getGuild()
                        .getIconUrl());

                // Now edit the message with the updated embed
                event.getChannel().editMessageById(adminEmbedMessageId, " ")
                        .setEmbeds(createProfileEmbed.build())
                        .setActionRow(createProfileForUserButton, nextPersonButton, previousPersonButton)
                        .queue();
            } else if (event.getComponentId().equals("previous-person")) {
                // Decrement the user index , wrapping around to the end of the list if necessary
                nonBotUserIndex--;
                if (nonBotUserIndex < 0) {
                    nonBotUserIndex = nonBotUsers.size() - 1; // Wrap to the last index if it goes below 0
                }

                User previousUser = nonBotUsers.get(nonBotUserIndex);

                // Update the createProfileEmbed with the information of the previous user
                createProfileEmbed.clear(); // Clear previous settings
                createProfileEmbed.setColor(Color.green); // Set the color again if needed
                createProfileEmbed.setTitle("Create profile for: " + previousUser.getName());
                createProfileEmbed.setDescription("You are now creating a profile for " + previousUser.getName()); // Example
                createProfileEmbed.setFooter("Request was made @ " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), event.getGuild().getIconUrl());

                // Now edit the message with the updated embed
                adminEmbed.clearFields();
                createProfileEmbed.clearFields();
                event.getChannel().editMessageById(adminEmbedMessageId, " ")
                        .setEmbeds(createProfileEmbed.build())
                        .setActionRow(createProfileForUserButton, nextPersonButton, previousPersonButton)
                        .queue();
            }
        }
            else if(event.getComponentId().equals("decrypt-msg")){
                // create the decryption modal here :)
                TextInput messageToDecrypt = TextInput.create("message-to-decrypt", "Decrypt message here!"
                                , TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter message: ")
                        .setRequired(true)
                        .build();
                // Correctly create the modal with action rows
                Modal modal = Modal.create("decrypt-msg", "Send a message to be decrypted")
                        .addActionRows( ActionRow.of(messageToDecrypt)).build();
                // Create the modal
                event.replyModal(modal).queue();
            }

    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        // Being completely honest, I think a lot of my code would be easier to read if I add "Switch-Case" statements
        // instead of if else statements.
        if(event.getModalId().equals("encryptMessage")){
            String string1 = (event.getValue("message")).getAsString();
            String encryptedMessage;
            try {
                 encryptedMessage = encrypt(string1, keyPair.getPublic());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            event.reply(encryptedMessage).setEphemeral(false).queue();
        }
        else if(event.getModalId().equals("decrypt-msg")){
            String messageToDecrypt = (Objects.requireNonNull(event.getValue("message-to-decrypt"))).getAsString();
            String decryptedMessage;
            try {
                decryptedMessage = decrypt(messageToDecrypt, keyPair.getPrivate());
                System.out.println();
                event.reply(decryptedMessage).setEphemeral(true).queue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}


