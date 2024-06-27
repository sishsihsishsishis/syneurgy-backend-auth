package com.bezkoder.spring.security.postgresql.config;

import com.bezkoder.spring.security.postgresql.models.ComponentNew;
import com.bezkoder.spring.security.postgresql.models.NBehavior;
import com.bezkoder.spring.security.postgresql.models.SubComponent;
import com.bezkoder.spring.security.postgresql.repository.ComponentNewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ComponentNewRepository componentNewRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create initial data
        Optional<ComponentNew> componentNewOptional1 = componentNewRepository.findByName("Trust");
        if (!componentNewOptional1.isPresent()) {
            ComponentNew componentNew = new ComponentNew("Trust", "Assurance that team members will support each other and act with integrity.");

            SubComponent subComponent1 = new SubComponent("Active listening",
                    "NodHead", "", componentNew
            );
            NBehavior behavior1 = new NBehavior("Nod head in agreement more often");
            NBehavior behavior2 = new NBehavior("Tilting your head can show interest");
            behavior1.setSubComponent(subComponent1);
            behavior2.setSubComponent(subComponent1);
            subComponent1.setBehaviors(Arrays.asList(
                    behavior1, behavior2
            ));
            SubComponent subComponent2 = new SubComponent("Active listening", "EyeContact", "", componentNew
            );

            NBehavior behavior3 = new NBehavior("Make sure you are looking at the speaker");
            NBehavior behavior4 = new NBehavior("Avoid multitasking when team members are speaking");
            NBehavior behavior5 = new NBehavior("Use expressive eyebrows");
            behavior3.setSubComponent(subComponent2);
            behavior4.setSubComponent(subComponent2);
            behavior5.setSubComponent(subComponent2);
            subComponent2.setBehaviors(Arrays.asList(
                    behavior3, behavior4, behavior5
            ));

            SubComponent subComponent3 = new SubComponent("Active listening", "LeanForward", "", componentNew
            );
            NBehavior behavior6 = new NBehavior("Lean forward to show you are actively listening");
            NBehavior behavior7 = new NBehavior("Try to avoid fidgeting, and refrain from constantly shifting around");
            behavior6.setSubComponent(subComponent3);
            behavior7.setSubComponent(subComponent3);
            subComponent3.setBehaviors(Arrays.asList(
                    behavior6, behavior7
            ));
            SubComponent subComponent4 = new SubComponent("Active listening", "", "NotInterrupting", componentNew
            );

            NBehavior behavior8 = new NBehavior("Be sure to control the pace of your speaking");
            NBehavior behavior9 = new NBehavior("Be sure to respectfully interrupt");
            NBehavior behavior10 = new NBehavior("Wait to speak until your team mates have completely finished");

            behavior8.setSubComponent(subComponent4);
            behavior9.setSubComponent(subComponent4);
            behavior10.setSubComponent(subComponent4);
            subComponent4.setBehaviors(Arrays.asList(
                    behavior8, behavior9, behavior10
            ));
            SubComponent subComponent5 = new SubComponent("Positive body language", "Smile", "", componentNew
            );

            NBehavior behavior11 = new NBehavior("Smiling can help improve team cohesion");
            behavior11.setSubComponent(subComponent5);
            subComponent5.setBehaviors(Arrays.asList(
                    behavior11
            ));
            SubComponent subComponent6 = new SubComponent("Positive body language", "NodHead", "", componentNew
            );

            NBehavior behavior12 = new NBehavior("Nod head in agreement more often");
            NBehavior behavior13 = new NBehavior("Tilting your head can show interest");
            behavior12.setSubComponent(subComponent6);
            behavior13.setSubComponent(subComponent6);
            subComponent6.setBehaviors(Arrays.asList(
                    behavior12, behavior13
            ));
            SubComponent subComponent7 = new SubComponent("Positive sentiment", "", "PositiveLanguage", componentNew
            );
            NBehavior behavior14 = new NBehavior("Use positive phrases like \"Great work!\", \"Thanks for your hard work.\" and \"Well done!\"");
            behavior14.setSubComponent(subComponent7);
            subComponent7.setBehaviors(Arrays.asList(
                    behavior14
            ));
            SubComponent subComponent8 = new SubComponent("Burstiness", "", "", componentNew
            );

            NBehavior behavior15 = new NBehavior("Acknowledge when others are speaking");
            behavior15.setSubComponent(subComponent8);
            subComponent8.setBehaviors(Arrays.asList(
                    behavior15
            ));

            componentNew.setSubComponents(Arrays.asList(subComponent1, subComponent2, subComponent3, subComponent4, subComponent5, subComponent6, subComponent7, subComponent8));
            componentNewRepository.save(componentNew);
        }

        Optional<ComponentNew> componentNewOptional2 = componentNewRepository.findByName("Psychological Safety");
        if (!componentNewOptional2.isPresent()) {
            ComponentNew componentNew = new ComponentNew("Psychological Safety", "A sense of security that allows team members to take risks and share their thoughts freely.");

            SubComponent subComponent9 = new SubComponent("Active listening", "NodHead", "", componentNew
            );
            NBehavior behavior16 = new NBehavior("Nod head in agreement more often");
            NBehavior behavior17 = new NBehavior("Tilting your head can show interest");
            behavior16.setSubComponent(subComponent9);
            behavior17.setSubComponent(subComponent9);
            subComponent9.setBehaviors(Arrays.asList(
                    behavior16, behavior17
            ));
            SubComponent subComponent10 = new SubComponent("Active listening", "EyeContact", "", componentNew
            );

            NBehavior behavior18 = new NBehavior("Make sure you are looking at the speaker");
            NBehavior behavior19 = new NBehavior("Avoid multitasking when team members are speaking");
            NBehavior behavior20 = new NBehavior("Use expressive eyebrows");
            behavior18.setSubComponent(subComponent10);
            behavior19.setSubComponent(subComponent10);
            behavior20.setSubComponent(subComponent10);
            subComponent10.setBehaviors(Arrays.asList(
                    behavior18, behavior19, behavior20
            ));
            SubComponent subComponent11 = new SubComponent("Active listening", "LeanForward", "", componentNew
            );
            NBehavior behavior21 = new NBehavior("Lean forward to show you are actively listening");
            NBehavior behavior22 = new NBehavior("Try to avoid fidgeting, and refrain from constantly shifting around");
            behavior21.setSubComponent(subComponent11);
            behavior22.setSubComponent(subComponent11);
            subComponent11.setBehaviors(Arrays.asList(
                    behavior21, behavior22
            ));
            SubComponent subComponent12 = new SubComponent("Relaxed Facial expression", "RelaxedFace", "", componentNew
            );
            NBehavior behavior23 = new NBehavior("Use relaxed facial expressions");
            behavior23.setSubComponent(subComponent12);
            subComponent12.setBehaviors(Arrays.asList(
                    behavior23
            ));
            SubComponent subComponent13 = new SubComponent("Open body language", "RelaxedPosture", "", componentNew
            );
            NBehavior behavior24 = new NBehavior("Relax any tension in your body");
            behavior24.setSubComponent(subComponent13);
            subComponent13.setBehaviors(Arrays.asList(
                    behavior24
            ));
            SubComponent subComponent14 = new SubComponent("", "", "NotInterrupting", componentNew
            );
            NBehavior behavior25 = new NBehavior("Be sure to control the pace of your speaking");
            NBehavior behavior26 = new NBehavior("Be sure to respectfully interrupt");
            NBehavior behavior27 = new NBehavior("Wait to speak until your team mates have completely finished");
            behavior25.setSubComponent(subComponent14);
            behavior26.setSubComponent(subComponent14);
            behavior27.setSubComponent(subComponent14);
            subComponent14.setBehaviors(Arrays.asList(
                    behavior25, behavior26, behavior27
            ));
            componentNew.setSubComponents(Arrays.asList(subComponent9, subComponent10, subComponent11, subComponent12, subComponent13, subComponent14));
            componentNewRepository.save(componentNew);
        }
        Optional<ComponentNew> componentNewOptional3 = componentNewRepository.findByName("Enjoyment");
        if (!componentNewOptional3.isPresent()) {
            ComponentNew componentNew = new ComponentNew("Enjoyment", "The positive feelings derived from collaborative efforts and interactions within the team.");

            SubComponent subComponent15 = new SubComponent("Positive body language", "Smile", "", componentNew
            );
            NBehavior behavior28 = new NBehavior("Smiling can help improve team cohesion");
            behavior28.setSubComponent(subComponent15);
            subComponent15.setBehaviors(Arrays.asList(
                    behavior28
            ));
            SubComponent subComponent16 = new SubComponent("Positive body language", "UprightPosture", "", componentNew
            );

            NBehavior behavior29 = new NBehavior("Make sure to sit up straight");
            NBehavior behavior30 = new NBehavior("Be mindful of of your body posture");
            behavior29.setSubComponent(subComponent16);
            behavior30.setSubComponent(subComponent16);
            subComponent16.setBehaviors(Arrays.asList(
                    behavior29, behavior30
            ));
            SubComponent subComponent17 = new SubComponent("Positive sentiment", "", "PositiveLanguage", componentNew
            );
            NBehavior behavior31 = new NBehavior("Use positive phrases like \"Great work!\", \"Thanks for your hard work.\" and \"Well done!\"");
            behavior31.setSubComponent(subComponent17);
            subComponent17.setBehaviors(Arrays.asList(
                    behavior31
            ));
            SubComponent subComponent18 = new SubComponent("Positive Emotion on face", "PositiveFace", "", componentNew
            );
            NBehavior behavior32 = new NBehavior("Make sure to keep  your eyes open and alert");
            behavior32.setSubComponent(subComponent18);
            subComponent18.setBehaviors(Arrays.asList(
                    behavior32
            ));
            componentNew.setSubComponents(Arrays.asList(subComponent15, subComponent16, subComponent17, subComponent18));
            componentNewRepository.save(componentNew);
        }

        Optional<ComponentNew> componentNewOptional4 = componentNewRepository.findByName("Engagement");
        if (!componentNewOptional4.isPresent()) {
            ComponentNew componentNew = new ComponentNew("Engagement", "Active involvement and investment in the team's tasks and objectives.");

            SubComponent subComponent19 = new SubComponent("Active participation", "RaiseHand", "", componentNew
            );
            NBehavior behavior33 = new NBehavior("Raise your hand when you want to make a contribution");
            NBehavior behavior34 = new NBehavior("Use hand gestures appropriately");
            behavior33.setSubComponent(subComponent19);
            behavior34.setSubComponent(subComponent19);
            subComponent19.setBehaviors(Arrays.asList(
                    behavior33, behavior34
            ));
            SubComponent subComponent20 = new SubComponent("Attention", "EyeContact", "", componentNew
            );
            NBehavior behavior35 = new NBehavior("Make sure you are looking at the speaker");
            NBehavior behavior36 = new NBehavior("Avoid multitasking when team members are speaking");
            NBehavior behavior37 = new NBehavior("Use expressive eyebrows");
            behavior35.setSubComponent(subComponent20);
            behavior36.setSubComponent(subComponent20);
            behavior37.setSubComponent(subComponent20);
            subComponent20.setBehaviors(Arrays.asList(
                    behavior35, behavior36, behavior37
            ));
            SubComponent subComponent21 = new SubComponent("", "FacingScreen", "", componentNew
            );
            NBehavior behavior38 = new NBehavior("Make sure to face the screen or show that you are listening");
            behavior38.setSubComponent(subComponent21);
            subComponent21.setBehaviors(Arrays.asList(
                    behavior38
            ));
            SubComponent subComponent22 = new SubComponent("", "", "EngageLanguage", componentNew
            );
            NBehavior behavior39 = new NBehavior("Ask open-ended questions");
            NBehavior behavior40 = new NBehavior("Actively use emojis or reactions to show engagement");
            NBehavior behavior41 = new NBehavior("Use language that includes all team members like \"our\", \"we\", and \"us\"");
            NBehavior behavior42 = new NBehavior("Intentionally validate your team members contributions");
            behavior39.setSubComponent(subComponent22);
            behavior40.setSubComponent(subComponent22);
            behavior41.setSubComponent(subComponent22);
            behavior42.setSubComponent(subComponent22);
            subComponent22.setBehaviors(Arrays.asList(
                    behavior39, behavior40, behavior41, behavior42
            ));
            componentNew.setSubComponents(Arrays.asList(subComponent19, subComponent20, subComponent21, subComponent22));
            componentNewRepository.save(componentNew);
        }

        Optional<ComponentNew> componentNewOptional5 = componentNewRepository.findByName("Participation");
        if (!componentNewOptional5.isPresent()) {
            ComponentNew componentNew = new ComponentNew("Participation", "Balanced involvement and contribution from all team members, fostering inclusivity."
            );

            SubComponent subComponent23 = new SubComponent("TurnTaking", "RaiseHand", "", componentNew
            );
            NBehavior behavior43 = new NBehavior("Raise your hand when you want to make a contribution");
            NBehavior behavior44 = new NBehavior("Use hand gestures appropriately");
            behavior43.setSubComponent(subComponent23);
            behavior44.setSubComponent(subComponent23);
            subComponent23.setBehaviors(Arrays.asList(
                    behavior43, behavior44
            ));
            SubComponent subComponent24 = new SubComponent("", "", "NotInterrupting", componentNew
            );
            NBehavior behavior45 = new NBehavior("Be sure to control the pace of your speaking");
            NBehavior behavior46 = new NBehavior("Be sure to respectfully interrupt");
            NBehavior behavior47 = new NBehavior("Wait to speak until your team mates have completely finished");
            behavior45.setSubComponent(subComponent24);
            behavior46.setSubComponent(subComponent24);
            behavior47.setSubComponent(subComponent24);
            subComponent23.setBehaviors(Arrays.asList(
                    behavior45, behavior46, behavior47
            ));
            componentNew.setSubComponents(Arrays.asList(subComponent23, subComponent24));
            componentNewRepository.save(componentNew);

        }

    }
}
