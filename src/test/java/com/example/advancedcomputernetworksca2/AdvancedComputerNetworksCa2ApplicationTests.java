package com.example.advancedcomputernetworksca2;

import com.example.advancedcomputernetworksca2.DiscordJDA.DecryptMessage;
import com.example.advancedcomputernetworksca2.DiscordJDA.Embeds;
import com.example.advancedcomputernetworksca2.DiscordJDA.SendMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdvancedComputerNetworksCa2ApplicationTests {

    public static void main(String[] args) {
        final String token = "INSERT-TOKEN-HERE";
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        JDA jda = jdaBuilder
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new Embeds())
                .addEventListeners(new SendMessage())
                .addEventListeners(new DecryptMessage())
                .build();
    }
}
