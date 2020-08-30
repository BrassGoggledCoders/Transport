package xyz.brassgoggledcoders.transport.screen.note;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

public class NoteLine {
    private final Style field_238795_a_;
    private final String field_238796_b_;
    private final ITextComponent field_238797_c_;
    private final int field_238798_d_;
    private final int field_238799_e_;

    public NoteLine(Style p_i232289_1_, String p_i232289_2_, int p_i232289_3_, int p_i232289_4_) {
        this.field_238795_a_ = p_i232289_1_;
        this.field_238796_b_ = p_i232289_2_;
        this.field_238798_d_ = p_i232289_3_;
        this.field_238799_e_ = p_i232289_4_;
        this.field_238797_c_ = (new StringTextComponent(p_i232289_2_)).setStyle(p_i232289_1_);
    }

    public Style getField_238795_a_() {
        return field_238795_a_;
    }

    public String getField_238796_b_() {
        return field_238796_b_;
    }

    public ITextComponent getField_238797_c_() {
        return field_238797_c_;
    }

    public int getField_238798_d_() {
        return field_238798_d_;
    }

    public int getField_238799_e_() {
        return field_238799_e_;
    }
}
