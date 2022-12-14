package pinacolada.skills.skills.base.modifiers;

import extendedui.EUIRM;
import extendedui.EUIUtils;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.misc.CombatManager;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_Affinity;

public class PMod_HighestAffinityBranch extends PMod_Branch<PField_Affinity, PCLAffinity>
{
    public static final PSkillData<PField_Affinity> DATA = register(PMod_HighestAffinityBranch.class, PField_Affinity.class)
            .pclOnly()
            .setExtra(-1, DEFAULT_MAX)
            .selfTarget();

    public PMod_HighestAffinityBranch(PSkillSaveData content)
    {
        super(content);
    }

    public PMod_HighestAffinityBranch(PCLAffinity... affinities)
    {
        super(DATA, PCLCardTarget.None, 1);
        fields.setAffinity(affinities);
    }

    public String getQualifier(int i)
    {
        PCLAffinity affinity = i < fields.affinities.size() ? fields.affinities.get(i) : null;
        return affinity != null ? affinity.getLevelTooltip().getTitleOrIcon() : TEXT.subjects.other;
    }

    @Override
    public boolean matchesBranch(PCLAffinity c, int i, PCLUseInfo info)
    {
        return i < fields.affinities.size() ? fields.affinities.get(i) == c : EUIUtils.all(fields.affinities, af -> c != af);
    }

    @Override
    public String getSampleText()
    {
        return TEXT.conditions.ifYourHighest(PGR.core.tooltips.affinityGeneral.title);
    }

    @Override
    public String getSubText()
    {
        String base = TEXT.conditions.ifYourHighest(EUIRM.strings.adjNoun(PGR.core.tooltips.level.title, PGR.core.tooltips.affinityGeneral.title));
        return extra > 0 ? base + " (" + TEXT.subjects.min(extra) + ")" : base;
    }

    @Override
    public void use(PCLUseInfo info)
    {
        int max = EUIUtils.max(PCLAffinity.getAvailableAffinities(), CombatManager.playerSystem::getLevel);
        if (max > extra)
        {
            branch(info, EUIUtils.filter(PCLAffinity.getAvailableAffinities(), af -> CombatManager.playerSystem.getLevel(af) >= max));
        }
    }
}
