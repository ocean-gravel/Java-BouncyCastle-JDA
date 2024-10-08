package com.example.advancedcomputernetworksca2.DiscordJDA;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DecryptMessage extends ListenerAdapter {


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event){
        if(event.getComponentId().equals("decrypt-msg")){

        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event){}



}
