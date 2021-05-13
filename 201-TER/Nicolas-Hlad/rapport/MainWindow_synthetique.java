...
public class MainWindow implements ITickListener
//#if CallButtons
    , ActionListener
//#endif
{
    ...
//#if CallButtons
    @Override
    public void onRequestFinished(Elevator elevator, Request request) {
//#if CallButtons
        ...
//#endif
//#if DirectedCall && CallButtons
        switch (request.getDirection()) {
            case MOVING_UP:
//#if DirectedCall && CallButtons
                listFloorComposites.get(request.getFloor()).resetUp();
//#endif
//#if DirectedCall3
                break;
//#endif
            case MOVING_DOWN:
//#if DirectedCall && CallButtons
                listFloorComposites.get(request.getFloor()).resetDown();
//#endif
//#if DirectedCall
                break;
//#endif
            default:
//#if DirectedCall
                break;
//#endif
        }
//#if DirectedCall
        ...
//#endif
//#endif
//#if UndirectedCall
        listFloorComposites.get(request.getFloor()).resetFloorRequest();
//#endif
    }
//#endif
    private void createPanelControlsContent(int maxFloors) {
        ...
//#if (( DirectedCall  &&  FloorPermission ) || ( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || ( FIFO  &&  Service )) && ! Sabbath
        gbc_btnService.insets = new Insets(0, 0, 0, 10);
//#endif
//#if (( Service  &&  Sabbath ) || ( FloorPermission  &&  Sabbath )) && ! DirectedCall  && ! FIFO  && ! ShortestPath  && ! UndirectedCall  && ! CallButtons
        gbc_btnService.insets = new Insets(0, 0, 0, 0);
//#endif
//#if (( Service  &&  Sabbath ) || ( FloorPermission  &&  Sabbath )) && ! DirectedCall  && ! FIFO  && ! ShortestPath  && ! UndirectedCall  && ! CallButtons
        gbc_btnService.gridwidth = 4;
//#endif
    ...
}