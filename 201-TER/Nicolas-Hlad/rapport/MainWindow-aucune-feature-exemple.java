public class MainWindow implements ITickListener

{
 ...
    private void clearPresent() {
        for (FloorComposite fl : listFloorComposites) {
            fl.showElevatorNotPresent();
        }
    }

    public void onTick(Elevator elevator) {
        ElevatorState state = elevator.getCurrentState();
        int currentFloor = elevator.getCurrentFloor();
        switch (state) {
        case MOVING_UP:
                    this.listFloorComposites.get(currentFloor - 1).showImageClose();
            break;
        case MOVING_DOWN:
                    this.listFloorComposites.get(currentFloor + 1).showImageClose();
            break;
        case FLOORING:
                    this.listFloorComposites.get(currentFloor).showImageOpen();
            break;
        }
        this.clearPresent();
        this.listFloorComposites.get(currentFloor).showElevatorIsPresent();
    }

    public void initialize(int maxFloors) {
        if(frmElevatorSample != null) {
            return;
        }
        frmElevatorSample = new JFrame();
        frmElevatorSample.setTitle("Elevator Sample");
        frmElevatorSample.setBounds(100, 50, 900, 650);
        frmElevatorSample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createBaseStructure();
        createPanelControlsContent(maxFloors);
        addBuilding(maxFloors);
        frmElevatorSample.setVisible(true);
    }
    ...
}