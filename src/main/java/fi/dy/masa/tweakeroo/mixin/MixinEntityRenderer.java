package fi.dy.masa.tweakeroo.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fi.dy.masa.tweakeroo.event.RenderEventHandler;
import fi.dy.masa.tweakeroo.renderer.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer
{
    @Inject(method = "renderWorldPass(IFJ)V", at = @At(
            value = "INVOKE_STRING",
            target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V",
            args = "ldc=hand"
        ))
    private void onRenderWorldLast(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci)
    {
        RenderEventHandler.onRenderWorldLast(partialTicks);
    }

    @Inject(method = "setupFog(IF)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;setFogDensity(F)V",
            shift = Shift.AFTER,
            ordinal = 4))
    private void onSetupLavaFog(int startCoords, float partialTicks, CallbackInfo ci)
    {
        RenderUtils.overrideLavaFog(Minecraft.getMinecraft().getRenderViewEntity());
    }
}
