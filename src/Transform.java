public class Transform {
    public Float3 position;
    public float yaw; // rotation around y-axis
    public float pitch; // rotation around x-axis

    public Float3 transformVector(Float3 ihat, Float3 jhat, Float3 khat, Float3 point) {
        Float3 ihatTransform = new Float3(point.getX()*ihat.getX(),point.getX()*ihat.getY(),point.getX()*ihat.getZ());
        Float3 jhatTransform = new Float3(point.getY()*jhat.getX(),point.getY()*jhat.getY(),point.getY()*jhat.getZ());
        Float3 khatTransform = new Float3(point.getZ()*khat.getX(),point.getZ()*khat.getY(),point.getZ()*khat.getZ());
        return ihatTransform.addFloat3(jhatTransform).addFloat3(khatTransform);
    }

    public Float3 toWorldPoint(Float3 point) {
        // Yaw
        Float3 ihat_y = new Float3((float) Math.cos(Math.toRadians(yaw)), 0, (float) Math.sin(Math.toRadians(yaw)));
        Float3 jhat_y = new Float3(0, 1, 0);
        Float3 khat_y = new Float3((float) -Math.sin(Math.toRadians(yaw)), 0, (float) Math.cos(Math.toRadians(yaw)));
        // Pitch
        Float3 ihat_p = new Float3(1, 0, 0);
        Float3 jhat_p = new Float3(0, (float) Math.cos(Math.toRadians(pitch)), (float) Math.sin(Math.toRadians(pitch)));
        Float3 khat_p = new Float3(0, (float) -Math.sin(Math.toRadians(pitch)),  (float) Math.cos(Math.toRadians(pitch)));

        Float3 ihat = transformVector(ihat_y, jhat_y, khat_y, ihat_p);
        Float3 jhat = transformVector(ihat_y, jhat_y, khat_y, jhat_p);
        Float3 khat = transformVector(ihat_y, jhat_y, khat_y, khat_p);

        Float3 transformWithoutPosShift = transformVector(ihat, jhat, khat, point);

        return new Float3(transformWithoutPosShift.getX() + position.getX(), transformWithoutPosShift.getY() + position.getY(), transformWithoutPosShift.getZ() + position.getZ());
    }

    public Float3 getPosition() { return position; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }

    public Transform(Float3 position, Float2 rotation) {
        this.position = position;
        this.yaw = rotation.getX();
        this.pitch = rotation.getY();
    }
}
