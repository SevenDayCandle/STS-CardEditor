package pinacolada.skills.fields;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.ui.cardEditor.PCLCustomCardEffectEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PField_CardModifyAffinity extends PField_CardCategory
{
    public ArrayList<PCLAffinity> addAffinities = new ArrayList<>();
    public boolean or;

    public PField_CardModifyAffinity()
    {
        super();
    }

    public PField_CardModifyAffinity(PField_CardModifyAffinity other)
    {
        super(other);
        setAddAffinity(other.addAffinities);
        setOr(other.or);
    }

    @Override
    public boolean equals(PField other)
    {
        return super.equals(other);
    }

    @Override
    public PField_CardModifyAffinity makeCopy()
    {
        return new PField_CardModifyAffinity(this);
    }

    public PField_CardModifyAffinity setAddAffinity(PCLAffinity... affinities)
    {
        return setAddAffinity(Arrays.asList(affinities));
    }

    public PField_CardModifyAffinity setAddAffinity(List<PCLAffinity> affinities)
    {
        this.addAffinities.clear();
        this.addAffinities.addAll(affinities);
        return this;
    }

    public PField_CardModifyAffinity setOr(boolean value)
    {
        this.or = value;
        return this;
    }

    public void setupEditor(PCLCustomCardEffectEditor editor)
    {
        editor.registerPile(groupTypes);
        editor.registerAffinity(addAffinities);
    }

    public String getAddAffinityChoiceString()
    {
        return getAffinityAndOrString(addAffinities, or);
    }
}
