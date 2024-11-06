package me.mochibit.defcon.effects

import me.mochibit.defcon.Defcon
import me.mochibit.defcon.extensions.toVector3f
import me.mochibit.defcon.lifecycle.Lifecycled
import me.mochibit.defcon.math.Transform3D
import me.mochibit.defcon.math.Vector3
import me.mochibit.defcon.observer.Loadable
import me.mochibit.defcon.particles.ParticleEmitter
import me.mochibit.defcon.particles.emitter.EmitterShape
import me.mochibit.defcon.particles.templates.AbstractParticle
import me.mochibit.defcon.vertexgeometry.particle.ParticleShape
import org.bukkit.Bukkit
import org.joml.Matrix4d
import org.joml.Matrix4f
import org.joml.Vector3d
import org.joml.Vector3f

/**
 * Represents an effect component that manages particle emission and transformation.
 */
open class ParticleComponent(
    private val particleEmitter: ParticleEmitter,
    private val colorSupplier: ColorSuppliable? = null,
) : EffectComponent {

    private var lifecycledSupport: Lifecycled? = colorSupplier as? Lifecycled

    // Matrix transformation for particleEmitter
    val transform: Matrix4f
        get() = particleEmitter.transform

    var visible: Boolean
        get() = particleEmitter.visible
        set(value) { particleEmitter.visible = value }

    var shape : EmitterShape
        get() = particleEmitter.emitterShape
        set(value) { particleEmitter.emitterShape = value }

    /**
     * Adds a spawnable particle with optional color supplier attachment.
     */
    fun addSpawnableParticle(particle: AbstractParticle, attachColorSupplier: Boolean = false): ParticleComponent {
        particleEmitter.spawnableParticles.add(particle)
        if (attachColorSupplier) {
            colorSupplier?.let { particle.colorSupplier(it.colorSupplier) }
        }
        return this
    }

    fun addSpawnableParticles(particles: List<AbstractParticle>, attachColorSupplier: Boolean = false): ParticleComponent {
        particleEmitter.spawnableParticles.addAll(particles)
        if (attachColorSupplier) {
            colorSupplier?.let { particles.forEach { it.colorSupplier(colorSupplier.colorSupplier) } }
        }
        return this
    }

    /**
     * Set the visibility of the particle component after a specified delay.
     */
    fun setVisibilityAfterDelay(visible: Boolean, delay: Long) = apply {
//        Bukkit.getScheduler().runTaskLaterAsynchronously(Defcon.instance) {
//            particleEmitter.visible = visible
//        }, delay)
    }

    /**
     * Translates the particle emitter by a specified vector.
     */
    fun translate(translation: Vector3f): ParticleComponent {
        transform.translate(translation, transform)
        return this
    }

    /**
     * Rotates the particle emitter around an axis by a specified angle.
     */
    fun rotate(axis: Vector3f, angle: Float): ParticleComponent {
        transform.rotate(angle, axis, transform)
        return this
    }

    /**
     * Apply radial velocity to particles moving them from the center outward.
     */
    fun applyRadialVelocityFromCenter(velocity: Vector3f) = apply {
        // Custom logic to apply radial velocity will go here when fully implemented.
        // Example:
        // particleEmitter.spawnableParticles.forEach {
        //     val direction = calculateDirectionFromCenter(it.position)
        //     it.velocity = direction.mul(velocity)
        // }
    }

    // Lifecycle management for starting, updating, and stopping the particle component.
    override fun start() {
        lifecycledSupport?.start()
        particleEmitter.start()
    }

    override fun update(delta: Float) {
        lifecycledSupport?.update(delta)
        particleEmitter.update(delta)
    }

    override fun stop() {
        lifecycledSupport?.stop()
        particleEmitter.stop()
    }
}
