package com.example.advancedcomputernetworksca2.DiscordJDA;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class SendMessage extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getComponentId().equals("send-msg")){

            TextInput messageToSend = TextInput.create("message", "Send a message!", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Enter message: ")
                    .setRequired(true)
                    .build();

            // Correctly create the modal with action rows
            Modal modal = Modal.create("encryptMessage", "Send a message to be encrypted")
                    .addActionRows( ActionRow.of(messageToSend)).build();
            // Create the modal
            event.replyModal(modal).queue();

        }
    }
}
