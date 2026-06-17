package com.linngdu664.bsf3lite.client.gui.texture;

public class Textures {
    public static final GuiSprite SNOWBALL_FRAME = new GuiSprite("snowball_frame", 23, 62);
    private static final GuiSprite TWEAKER_FRAME = new GuiSprite("tweaker_frame", 114, 106);
    public static final GuiSubSprite TWEAKER_LOCATOR_GUI = new GuiSubSprite(TWEAKER_FRAME, 1, 0, 22, 42);
    public static final GuiSubSprite TWEAKER_STATUS_GUI = new GuiSubSprite(TWEAKER_FRAME, 24, 0, 22, 102);
    public static final GuiSubSprite TWEAKER_SELECTOR_GUI = new GuiSubSprite(TWEAKER_FRAME, 0, 82, 24, 24);
    public static final GuiSubSprite GOLEM_LOCATOR_GUI = new GuiSubSprite(TWEAKER_FRAME, 47, 0, 22, 42);
    public static final GuiSubSprite GOLEM_STATUS_GUI = new GuiSubSprite(TWEAKER_FRAME, 70, 0, 22, 102);
    public static final GuiSubSprite GOLEM_SELECTOR_GUI = new GuiSubSprite(TWEAKER_FRAME, 46, 82, 24, 24);
    public static final GuiSubSprite SETTER_ARROW_GUI = new GuiSubSprite(TWEAKER_FRAME, 92, 1, 8, 20);
    public static final GuiSubSprite ADVANCE_MODE_GUI = new GuiSubSprite(TWEAKER_FRAME, 92, 60, 22, 22);
    public static final GuiSubSprite EQUIPMENT_SLOT_FRAME_GUI = new GuiSubSprite(TWEAKER_FRAME, 92, 84, 22, 22);
}
