package com.linngdu664.bsf3lite.entity.ai.goal;

import com.linngdu664.bsf3lite.entity.golem.AbstractBSFSnowGolemEntity;
import com.linngdu664.bsf3lite.entity.golem.HostileSnowGolemEntity;
import com.linngdu664.bsf3lite.item.weapon.AbstractBSFWeaponItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf3lite.registry.EffectRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import com.linngdu664.bsf3lite.util.BSFCommonUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class HostileGolemRangedAttackGoal extends Goal {
    private final AbstractBSFSnowGolemEntity golem;
    protected final double speedModifier;
    protected final int attackInterval;
    protected final float attackRadius;
    protected final float attackRadiusSqr;
    protected int seeTime;
    protected int attackTime = -1;
    protected int strafingTime = -1;
    protected boolean strafingClockwise;
    protected boolean strafingBackwards;
    protected @NotNull Vec3 lastPos = Vec3.ZERO;

    public HostileGolemRangedAttackGoal(AbstractBSFSnowGolemEntity golem, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
        this.golem = golem;
        this.speedModifier = pSpeedModifier;
        this.attackInterval = pAttackInterval;
        this.attackRadius = pAttackRadius;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = golem.getTarget();
        if (target != null && target.isAlive()) {
            lastPos = target.getEyePosition();
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse() || !golem.getNavigation().isDone();
    }

    @Override
    public void stop() {
        seeTime = 0;
        attackTime = -1;
        golem.setZza(0);
        golem.setXxa(0);
        golem.setSpeed(0);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    // todo 末影龙
    protected boolean canShoot(LivingEntity pTarget) {
        ItemStack weapon = golem.getWeapon();
        if (weapon.isEmpty() || !(weapon.getItem() instanceof AbstractBSFWeaponItem weaponItem) || golem.hasEffect(EffectRegistry.WEAPON_JAM)) {
            return false;
        }
        float launchV = 3.0F;
        float acc = 1.0F;
        if (weaponItem.equals(ItemRegistry.POWERFUL_SNOWBALL_CANNON.get())) {
            launchV = 4.0F;
        } else if (weaponItem.equals(ItemRegistry.SNOWBALL_SHOTGUN.get())) {
            launchV = 2.0F;
            acc = 10.0F;
        }

        // 计算发射角度
        double v = launchV * 0.98;  // 考虑到阻力，计算时假设初速度慢一点
        double h = pTarget.getEyeY() - golem.getEyeY();
        double dx = pTarget.getX() - golem.getX();
        double dz = pTarget.getZ() - golem.getZ();
        double x2 = BSFCommonUtil.lengthSqr(dx, dz);
        double d = Math.sqrt(x2 + h * h);   // d 是总距离
        double x = Math.sqrt(x2);       // x 是水平距离
        double cosTheta, sinTheta;
        double k = 0.015 * x2 / (v * v);    // 0.5 * g / 400.0, 其中 g = 12
        cosTheta = 0.7071067811865475 / d * Math.sqrt(x2 - 2 * k * h + x * Math.sqrt(x2 - 4 * k * k - 4 * k * h));
        if (golem.isPredictMotion()) {
            // 假设目标做匀速直线运动（因为无法得知是飞行还是下落），近似提前量
            Vec3 vel = pTarget.getEyePosition().subtract(lastPos);
            double t = x / (v * cosTheta);      // t 是弹射物飞行时间
            h += t * vel.y;
            dx += t * vel.x;
            dz += t * vel.z;
            x2 = BSFCommonUtil.lengthSqr(dx, dz);
            d = Math.sqrt(x2 + h * h);
            x = Math.sqrt(x2);
            k = 0.015 * x2 / (v * v);
            cosTheta = 0.7071067811865475 / d * Math.sqrt(x2 - 2 * k * h + x * Math.sqrt(x2 - 4 * k * k - 4 * k * h));
        }
        if (cosTheta > 1) {
            sinTheta = 0;
        } else {
            sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
            if (h < -k) {
                sinTheta = -sinTheta;
            }
        }
        dx = dx / x * cosTheta;
        dz = dz / x * cosTheta;

        // 防误伤机制
        List<LivingEntity> list = golem.level().getEntitiesOfClass(LivingEntity.class, golem.getBoundingBox().inflate(x), p -> !p.isSpectator() && !golem.equals(p) && !pTarget.equals(p));
        for (LivingEntity entity : list) {
            double dx1 = entity.getX() - golem.getX();
            double dz1 = entity.getZ() - golem.getZ();
            double cosAlpha = BSFCommonUtil.vec2AngleCos(dx, dz, dx1, dz1);
            if (cosAlpha <= 0.17) {
                continue;
            }
            AABB aabb = entity.getBoundingBox();
            double sinAlpha = Math.sqrt(1 - cosAlpha * cosAlpha);
            double r = Math.sqrt(BSFCommonUtil.lengthSqr(dx1, dz1));
            if (r < x && r * sinAlpha < Math.sqrt(BSFCommonUtil.lengthSqr(aabb.maxX - aabb.minX, aabb.maxZ - aabb.minZ)) * 0.5 + 0.8) {
                double t = r * cosAlpha / (v * cosTheta);
                double y = v * sinTheta * t - 0.015 * t * t + golem.getEyeY();
                if (y >= aabb.minY - 0.8 && y <= aabb.maxY + 0.8) {
                    changeTargetWhenNecessary(entity);
                    return false;
                }
            }
        }

        golem.setShootX(dx);
        golem.setShootY(sinTheta);
        golem.setShootZ(dz);
        golem.setLaunchAccuracy(acc);
        golem.setLaunchVelocity(launchV);
        return true;
    }

    protected void changeTargetWhenNecessary(LivingEntity entity) {
        if (!(entity instanceof HostileSnowGolemEntity)) {
            golem.setTarget(entity);
        }
        attackTime = 1;
    }

    public void tick() {
        LivingEntity target = golem.getTarget();
        if (target != null) {
            float attackRadiusSqr = this.attackRadiusSqr;
            float attackRadius = this.attackRadius;
            if (golem.getWeapon().getItem() instanceof SnowballShotgunItem) {
                attackRadius *= 0.2F;
                attackRadiusSqr *= 0.04F;
            }
            double d0 = golem.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean flag = golem.getSensing().hasLineOfSight(target);
            boolean flag1 = seeTime > 0;
            if (flag != flag1) {
                seeTime = 0;
            }
            if (flag) {
                ++seeTime;
            } else {
                --seeTime;
            }
            if (d0 <= attackRadiusSqr && seeTime >= 20 || !golem.canMoveAndAttack()) {
                golem.getNavigation().stop();
                ++strafingTime;
            } else {
                golem.getNavigation().moveTo(target, speedModifier);
                strafingTime = -1;
            }
            if (strafingTime >= 20) {
                if (golem.getRandom().nextFloat() < 0.3F) {
                    strafingClockwise = !strafingClockwise;
                }
                if (golem.getRandom().nextFloat() < 0.3F) {
                    strafingBackwards = !strafingBackwards;
                }
                strafingTime = 0;
            }
            if (strafingTime > -1 && golem.canMoveAndAttack()) {
                if (d0 > attackRadiusSqr * 0.64F) {
                    strafingBackwards = false;
                } else if (d0 < attackRadiusSqr * 0.09F) {
                    strafingBackwards = true;
                }
                golem.getMoveControl().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.5F : -0.5F);
                golem.lookAt(target, 30.0F, 30.0F);
            } else {
                golem.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            if (--attackTime <= 0) {
                if (attackTime == 0) {
                    if (!flag || !canShoot(target)) {
                        return;
                    }
                    float f = (float) Math.sqrt(d0) / attackRadius;
                    float f1 = Mth.clamp(f, 0.1F, 1.0F);
                    golem.performRangedAttack(target, f1);
                }
                attackTime = attackInterval;
            }
            lastPos = target.getEyePosition();
        }
    }
}
