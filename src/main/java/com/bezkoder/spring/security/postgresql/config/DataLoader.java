package com.bezkoder.spring.security.postgresql.config;

import com.bezkoder.spring.security.postgresql.models.ComponentM;
import com.bezkoder.spring.security.postgresql.models.MBehavior;
import com.bezkoder.spring.security.postgresql.repository.ComponentMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ComponentMRepository componentMRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create initial data
        ComponentM component1 = new ComponentM();
        component1.setComponent("Trust");
        component1.setSubcomponent("Active listening");
        component1.setCv("NodHead");
        component1.setNlp("");
        component1.setBehaviors(Arrays.asList(
                new MBehavior(1, "Nod head in agreement more often"),
                new MBehavior(2, "Tilting your head can show interest")
        ));

        Optional<ComponentM> existingComponent1 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component1.getComponent(),
                        component1.getSubcomponent(),
                        component1.getCv(),
                        component1.getNlp()
                );

        if (!existingComponent1.isPresent()) {
            componentMRepository.save(component1);
        }

        ComponentM component2 = new ComponentM();
        component2.setComponent("Trust");
        component2.setSubcomponent("Active listening");
        component2.setCv("EyeContact");
        component2.setNlp("");
        component2.setBehaviors(Arrays.asList(
                new MBehavior(1, "Make sure you are looking at the speaker"),
                new MBehavior(2, "Avoid multitasking when team members are speaking"),
                new MBehavior(3, "Use expressive eyebrows")
        ));

        Optional<ComponentM> existingComponent2 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component2.getComponent(),
                        component2.getSubcomponent(),
                        component2.getCv(),
                        component2.getNlp()
                );

        if (!existingComponent2.isPresent()) {
            componentMRepository.save(component2);
        }

        ComponentM component3 = new ComponentM();
        component3.setComponent("Trust");
        component3.setSubcomponent("Active listening");
        component3.setCv("LeanForward");
        component3.setNlp("");
        component3.setBehaviors(Arrays.asList(
                new MBehavior(1, "Lean forward to show you are actively listening"),
                new MBehavior(2, "Try to avoid fidgeting, and refrain from constantly shifting around")
        ));

        Optional<ComponentM> existingComponent3 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component3.getComponent(),
                        component3.getSubcomponent(),
                        component3.getCv(),
                        component3.getNlp()
                );

        if (!existingComponent3.isPresent()) {
            componentMRepository.save(component3);
        }

        ComponentM component4 = new ComponentM();
        component4.setComponent("Trust");
        component4.setSubcomponent("Active listening");
        component4.setCv("");
        component4.setNlp("NotInterrupting");
        component4.setBehaviors(Arrays.asList(
                new MBehavior(1, "Be sure to control the pace of your speaking"),
                new MBehavior(2, "Be sure to respectfully interrupt"),
                new MBehavior(3, "Wait to speak until your team mates have completely finished")
        ));

        Optional<ComponentM> existingComponent4 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component4.getComponent(),
                        component4.getSubcomponent(),
                        component4.getCv(),
                        component4.getNlp()
                );

        if (!existingComponent4.isPresent()) {
            componentMRepository.save(component4);
        }

        ComponentM component5 = new ComponentM();
        component5.setComponent("Trust");
        component5.setSubcomponent("Positive body language");
        component5.setCv("Smile");
        component5.setNlp("");
        component5.setBehaviors(Arrays.asList(
                new MBehavior(1, "Smiling can help improve team cohesion")
        ));

        Optional<ComponentM> existingComponent5 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component5.getComponent(),
                        component5.getSubcomponent(),
                        component5.getCv(),
                        component5.getNlp()
                );

        if (!existingComponent5.isPresent()) {
            componentMRepository.save(component5);
        }

        ComponentM component6 = new ComponentM();
        component6.setComponent("Trust");
        component6.setSubcomponent("Positive body language");
        component6.setCv("NodHead");
        component6.setNlp("");
        component6.setBehaviors(Arrays.asList(
                new MBehavior(1, "Nod head in agreement more often"),
                new MBehavior(2, "Tilting your head can show interest")
        ));

        Optional<ComponentM> existingComponent6 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component6.getComponent(),
                        component6.getSubcomponent(),
                        component6.getCv(),
                        component6.getNlp()
                );

        if (!existingComponent6.isPresent()) {
            componentMRepository.save(component6);
        }

        ComponentM component7 = new ComponentM();
        component7.setComponent("Trust");
        component7.setSubcomponent("Positive sentiment");
        component7.setCv("");
        component7.setNlp("PositiveLanguage");
        component7.setBehaviors(Arrays.asList(
                new MBehavior(1, "Use positive phrases like \"Great work!\", \"Thanks for your hard work.\" and \"Well done!\"")
        ));

        Optional<ComponentM> existingComponent7 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component7.getComponent(),
                        component7.getSubcomponent(),
                        component7.getCv(),
                        component7.getNlp()
                );

        if (!existingComponent7.isPresent()) {
            componentMRepository.save(component7);
        }

        ComponentM component8 = new ComponentM();
        component8.setComponent("Trust");
        component8.setSubcomponent("Burstiness");
        component8.setCv("");
        component8.setNlp("");
        component8.setBehaviors(Arrays.asList(
                new MBehavior(1, "Acknowledge when others are speaking")
        ));

        Optional<ComponentM> existingComponent8 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component8.getComponent(),
                        component8.getSubcomponent(),
                        component8.getCv(),
                        component8.getNlp()
                );

        if (!existingComponent8.isPresent()) {
            componentMRepository.save(component8);
        }


        ComponentM component9 = new ComponentM();
        component9.setComponent("Psychological Safety");
        component9.setSubcomponent("Active listening");
        component9.setCv("NodHead");
        component9.setNlp("");
        component9.setBehaviors(Arrays.asList(
                new MBehavior(1, "Nod head in agreement more often"),
                new MBehavior(2, "Tilting your head can show interest")
        ));

        Optional<ComponentM> existingComponent9 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component9.getComponent(),
                        component9.getSubcomponent(),
                        component9.getCv(),
                        component9.getNlp()
                );

        if (!existingComponent9.isPresent()) {
            componentMRepository.save(component9);
        }

        ComponentM component10 = new ComponentM();
        component10.setComponent("Psychological Safety");
        component10.setSubcomponent("Active listening");
        component10.setCv("EyeContact");
        component10.setNlp("");
        component10.setBehaviors(Arrays.asList(
                new MBehavior(1, "Make sure you are looking at the speaker"),
                new MBehavior(2, "Avoid multitasking when team members are speaking"),
                new MBehavior(3, "Use expressive eyebrows")
        ));

        Optional<ComponentM> existingComponent10 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component10.getComponent(),
                        component10.getSubcomponent(),
                        component10.getCv(),
                        component10.getNlp()
                );

        if (!existingComponent10.isPresent()) {
            componentMRepository.save(component10);
        }

        ComponentM component11 = new ComponentM();
        component11.setComponent("Psychological Safety");
        component11.setSubcomponent("Active listening");
        component11.setCv("LeanForward");
        component11.setNlp("");
        component11.setBehaviors(Arrays.asList(
                new MBehavior(1, "Lean forward to show you are actively listening"),
                new MBehavior(2, "Try to avoid fidgeting, and refrain from constantly shifting around")
        ));

        Optional<ComponentM> existingComponent11 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component11.getComponent(),
                        component11.getSubcomponent(),
                        component11.getCv(),
                        component11.getNlp()
                );

        if (!existingComponent11.isPresent()) {
            componentMRepository.save(component11);
        }

        ComponentM component12 = new ComponentM();
        component12.setComponent("Psychological Safety");
        component12.setSubcomponent("Relaxed Facial expression");
        component12.setCv("RelaxedFace");
        component12.setNlp("");
        component12.setBehaviors(Arrays.asList(
                new MBehavior(1, "Use relaxed facial expressions"),
                new MBehavior(2, "Try to avoid fidgeting, and refrain from constantly shifting around")
        ));

        Optional<ComponentM> existingComponent12 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component12.getComponent(),
                        component12.getSubcomponent(),
                        component12.getCv(),
                        component12.getNlp()
                );

        if (!existingComponent12.isPresent()) {
            componentMRepository.save(component12);
        }

        ComponentM component13 = new ComponentM();
        component13.setComponent("Psychological Safety");
        component13.setSubcomponent("Open body language");
        component13.setCv("RelaxedPosture");
        component13.setNlp("");
        component13.setBehaviors(Arrays.asList(
                new MBehavior(1, "Relax any tension in your body")
        ));

        Optional<ComponentM> existingComponent13 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component13.getComponent(),
                        component13.getSubcomponent(),
                        component13.getCv(),
                        component13.getNlp()
                );

        if (!existingComponent13.isPresent()) {
            componentMRepository.save(component13);
        }

        ComponentM component14 = new ComponentM();
        component14.setComponent("Psychological Safety");
        component14.setSubcomponent("");
        component14.setCv("");
        component14.setNlp("NotInterrupting");
        component14.setBehaviors(Arrays.asList(
                new MBehavior(1, "Be sure to control the pace of your speaking"),
                new MBehavior(2, "Be sure to respectfully interrupt"),
                new MBehavior(3, "Wait to speak until your team mates have completely finished")
        ));

        Optional<ComponentM> existingComponent14 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component14.getComponent(),
                        component14.getSubcomponent(),
                        component14.getCv(),
                        component14.getNlp()
                );

        if (!existingComponent14.isPresent()) {
            componentMRepository.save(component14);
        }

        ComponentM component15 = new ComponentM();
        component15.setComponent("Enjoyment");
        component15.setSubcomponent("Positive body language");
        component15.setCv("Smile");
        component15.setNlp("");
        component15.setBehaviors(Arrays.asList(
                new MBehavior(1, "Smiling can help improve team cohesion")
        ));

        Optional<ComponentM> existingComponent15 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component15.getComponent(),
                        component15.getSubcomponent(),
                        component15.getCv(),
                        component15.getNlp()
                );

        if (!existingComponent15.isPresent()) {
            componentMRepository.save(component15);
        }

        ComponentM component16 = new ComponentM();
        component16.setComponent("Enjoyment");
        component16.setSubcomponent("Positive body language");
        component16.setCv("UprightPosture");
        component16.setNlp("");
        component16.setBehaviors(Arrays.asList(
                new MBehavior(1, "Make sure to sit up straight"),
                new MBehavior(2, "Be mindful of of your body posture")
        ));

        Optional<ComponentM> existingComponent16 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component16.getComponent(),
                        component16.getSubcomponent(),
                        component16.getCv(),
                        component16.getNlp()
                );

        if (!existingComponent16.isPresent()) {
            componentMRepository.save(component16);
        }

        ComponentM component17 = new ComponentM();
        component17.setComponent("Enjoyment");
        component17.setSubcomponent("Positive sentiment");
        component17.setCv("");
        component17.setNlp("PositiveLanguage");
        component17.setBehaviors(Arrays.asList(
                new MBehavior(1, "Use positive phrases like \"Great work!\", \"Thanks for your hard work.\" and \"Well done!\"")
        ));

        Optional<ComponentM> existingComponent17 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component17.getComponent(),
                        component17.getSubcomponent(),
                        component17.getCv(),
                        component17.getNlp()
                );

        if (!existingComponent17.isPresent()) {
            componentMRepository.save(component17);
        }

        ComponentM component18 = new ComponentM();
        component18.setComponent("Enjoyment");
        component18.setSubcomponent("Positive Emotion on face");
        component18.setCv("PositiveFace");
        component18.setNlp("");
        component18.setBehaviors(Arrays.asList(
                new MBehavior(1, "Make sure to keep  your eyes open and alert")
        ));

        Optional<ComponentM> existingComponent18 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component18.getComponent(),
                        component18.getSubcomponent(),
                        component18.getCv(),
                        component18.getNlp()
                );

        if (!existingComponent18.isPresent()) {
            componentMRepository.save(component18);
        }

        ComponentM component19 = new ComponentM();
        component19.setComponent("Engagement");
        component19.setSubcomponent("Active participation");
        component19.setCv("RaiseHand");
        component19.setNlp("");
        component19.setBehaviors(Arrays.asList(
                new MBehavior(1, "Raise your hand when you want to make a contribution"),
                new MBehavior(2, "Use hand gestures appropriately")
        ));

        Optional<ComponentM> existingComponent19 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component19.getComponent(),
                        component19.getSubcomponent(),
                        component19.getCv(),
                        component19.getNlp()
                );

        if (!existingComponent19.isPresent()) {
            componentMRepository.save(component19);
        }

        ComponentM component20 = new ComponentM();
        component20.setComponent("Engagement");
        component20.setSubcomponent("Attention");
        component20.setCv("EyeContact");
        component20.setNlp("");
        component20.setBehaviors(Arrays.asList(
                new MBehavior(1, "Make sure you are looking at the speaker"),
                new MBehavior(2, "Avoid multitasking when team members are speaking"),
                new MBehavior(3, "Use expressive eyebrows")
        ));

        Optional<ComponentM> existingComponent20 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component20.getComponent(),
                        component20.getSubcomponent(),
                        component20.getCv(),
                        component20.getNlp()
                );

        if (!existingComponent20.isPresent()) {
            componentMRepository.save(component20);
        }

        ComponentM component21 = new ComponentM();
        component21.setComponent("Engagement");
        component21.setSubcomponent("");
        component21.setCv("FacingScreen");
        component21.setNlp("");
        component21.setBehaviors(Arrays.asList(
                new MBehavior(1, "Make sure to face the screen or show that you are listening")
        ));

        Optional<ComponentM> existingComponent21 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component21.getComponent(),
                        component21.getSubcomponent(),
                        component21.getCv(),
                        component21.getNlp()
                );

        if (!existingComponent21.isPresent()) {
            componentMRepository.save(component21);
        }

        ComponentM component22 = new ComponentM();
        component22.setComponent("Engagement");
        component22.setSubcomponent("");
        component22.setCv("");
        component22.setNlp("EngageLanguage");
        component22.setBehaviors(Arrays.asList(
                new MBehavior(1, "Ask open-ended questions"),
                new MBehavior(2, "Actively use emojis or reactions to show engagement"),
                new MBehavior(3, "Use language that includes all team members like \"our\", \"we\", and \"us\""),
                new MBehavior(4, "Intentionally validate your team members contributions")
        ));

        Optional<ComponentM> existingComponent22 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component22.getComponent(),
                        component22.getSubcomponent(),
                        component22.getCv(),
                        component22.getNlp()
                );

        if (!existingComponent22.isPresent()) {
            componentMRepository.save(component22);
        }

        ComponentM component23 = new ComponentM();
        component23.setComponent("Participation");
        component23.setSubcomponent("TurnTaking");
        component23.setCv("RaiseHand");
        component23.setNlp("");
        component23.setBehaviors(Arrays.asList(
                new MBehavior(1, "Raise your hand when you want to make a contribution"),
                new MBehavior(2, "Use hand gestures appropriately")
        ));

        Optional<ComponentM> existingComponent23 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component23.getComponent(),
                        component23.getSubcomponent(),
                        component23.getCv(),
                        component23.getNlp()
                );

        if (!existingComponent23.isPresent()) {
            componentMRepository.save(component23);
        }


        ComponentM component24 = new ComponentM();
        component24.setComponent("Participation");
        component24.setSubcomponent("");
        component24.setCv("");
        component24.setNlp("NotInterrupting");
        component24.setBehaviors(Arrays.asList(
                new MBehavior(1, "Be sure to control the pace of your speaking"),
                new MBehavior(2, "Be sure to respectfully interrupt"),
                new MBehavior(3, "Wait to speak until your team mates have completely finished")
        ));
        Optional<ComponentM> existingComponent24 = componentMRepository
                .findByComponentAndSubcomponentAndCvAndNlp(
                        component24.getComponent(),
                        component24.getSubcomponent(),
                        component24.getCv(),
                        component24.getNlp()
                );

        if (!existingComponent24.isPresent()) {
            componentMRepository.save(component24);
        }
    }
}