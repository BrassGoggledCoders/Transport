package xyz.brassgoggledcoders.transport.screen.note;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.text.Style;

public class NotePage {
    public static final NotePage field_238779_a_ = new NotePage("", new Point(0, 0), true, new int[]{0}, new NoteLine[]{new NoteLine(Style.EMPTY, "", 0, 0)}, new Rectangle2d[0]);
    private final String field_238780_b_;
    private final Point field_238781_c_;
    private final boolean field_238782_d_;
    private final int[] field_238783_e_;
    private final NoteLine[] field_238784_f_;
    private final Rectangle2d[] field_238785_g_;

    public NotePage(String p_i232288_1_, Point p_i232288_2_, boolean p_i232288_3_, int[] p_i232288_4_, NoteLine[] p_i232288_5_, Rectangle2d[] p_i232288_6_) {
        this.field_238780_b_ = p_i232288_1_;
        this.field_238781_c_ = p_i232288_2_;
        this.field_238782_d_ = p_i232288_3_;
        this.field_238783_e_ = p_i232288_4_;
        this.field_238784_f_ = p_i232288_5_;
        this.field_238785_g_ = p_i232288_6_;
    }

    public int func_238789_a_(FontRenderer p_238789_1_, Point p_238789_2_) {
        int i = p_238789_2_.getY() / 9;
        if (i < 0) {
            return 0;
        } else if (i >= this.field_238784_f_.length) {
            return this.field_238780_b_.length();
        } else {
            NoteLine editbookscreen$bookline = this.field_238784_f_[i];
            return this.field_238783_e_[i] + p_238789_1_.func_238420_b_().func_238352_a_(editbookscreen$bookline.getField_238796_b_(), p_238789_2_.getX(),
                    editbookscreen$bookline.getField_238795_a_());
        }
    }

    public int func_238788_a_(int p_238788_1_, int p_238788_2_) {
        int i = EditNoteScreen.func_238768_b_(this.field_238783_e_, p_238788_1_);
        int j = i + p_238788_2_;
        int k;
        if (0 <= j && j < this.field_238783_e_.length) {
            int l = p_238788_1_ - this.field_238783_e_[i];
            int i1 = this.field_238784_f_[j].getField_238796_b_().length();
            k = this.field_238783_e_[j] + Math.min(l, i1);
        } else {
            k = p_238788_1_;
        }

        return k;
    }

    public int func_238787_a_(int p_238787_1_) {
        int i = EditNoteScreen.func_238768_b_(this.field_238783_e_, p_238787_1_);
        return this.field_238783_e_[i];
    }

    public int func_238791_b_(int p_238791_1_) {
        int i = EditNoteScreen.func_238768_b_(this.field_238783_e_, p_238791_1_);
        return this.field_238783_e_[i] + this.field_238784_f_[i].getField_238796_b_().length();
    }

    public Point getField_238781_c_() {
        return field_238781_c_;
    }

    public boolean isField_238782_d_() {
        return field_238782_d_;
    }

    public Rectangle2d[] getField_238785_g_() {
        return field_238785_g_;
    }

    public NoteLine[] getField_238784_f_() {
        return field_238784_f_;
    }
}
