package mrscenariofootball.core.data.worlddata.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "playMode")
@XmlEnum
public enum PlayMode {

    @XmlEnumValue("kick off") KickOff,
    @XmlEnumValue("play on") PlayOn,
    @XmlEnumValue("corner kick blue") CornerKickBlue,
    @XmlEnumValue("corner kick yellow") CornerKickYellow,
    @XmlEnumValue("goal kick blue") GoalKickBlue,
    @XmlEnumValue("goal kick yellow") GoalKickYellow,
    @XmlEnumValue("time out blue") TimeOutBlue,
    @XmlEnumValue("time out yellow") TimeOutYellow,
    @XmlEnumValue("frozen operator") FrozenOperator,
    @XmlEnumValue("frozen match") FrozenMatch,
    @XmlEnumValue("penalty") Penalty,
    @XmlEnumValue("warn ending") WarnEnding,
    @XmlEnumValue("warming up") WarmingUp,
    @XmlEnumValue("time over") TimeOver,
    @XmlEnumValue("team adjustmest") TeamAdjustment;
    
}