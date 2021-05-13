//#if FloorPermission
import java.util.Arrays;
//#endif
//#if Service
import java.awt.Dimension;
//#endif
//#if CallButtons
import de.ovgu.featureide.examples.elevator.core.controller.Request;
//#endif
//#if (( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || DirectedCall || FIFO || ( FloorPermission  &&  Sabbath ) || ( Service  &&  Sabbath ))
import javax.swing.JToggleButton;
//#endif
//#if (( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || DirectedCall || FIFO || ( FloorPermission  &&  Sabbath ) || ( Service  &&  Sabbath ))
import java.awt.event.ActionEvent;
//#endif
//#if (( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || DirectedCall || FIFO || ( FloorPermission  &&  Sabbath ) || ( Service  &&  Sabbath ))
import java.awt.event.ActionListener;
//#endif
//#if (( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || DirectedCall || FIFO || ( FloorPermission  &&  Sabbath ) || ( Service  &&  Sabbath ))
import java.awt.GridLayout;
//#endif
//#if (( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || DirectedCall || FIFO || ( FloorPermission  &&  Sabbath ) || ( Service  &&  Sabbath ))
import de.ovgu.featureide.examples.elevator.sim.SimulationUnit;
//#endif
public class MainWindow implements ITickListener
//#if CallButtons
    , ActionListener
//#endif
{
    private JFrame frmElevatorSample;
    private JSplitPane splitPane;
    private JLabel lblEvent;
    private List<FloorComposite> listFloorComposites = new ArrayList<>();
//#if CallButtons
    private List<JToggleButton> listInnerElevatorControls = new ArrayList<>();
//#endif
//#if (( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || DirectedCall || FIFO || ( FloorPermission  &&  Sabbath ) || ( Service  &&  Sabbath ))
    private SimulationUnit sim;
//#endif
//#if (( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || DirectedCall || FIFO || ( FloorPermission  &&  Sabbath ) || ( Service  &&  Sabbath ))
    public MainWindow(SimulationUnit sim) {
        this.sim = sim;
    }
//#endif
    private void clearPresent() {
        for (FloorComposite fl : listFloorComposites) {
            fl.showElevatorNotPresent();
        }
    }
//#if CallButtons
    @Override
    public void onRequestFinished(Elevator elevator, Request request) {
//#if DirectedCall
        switch (request.getDirection()) {
            case MOVING_UP:
//#if DirectedCall
                listFloorComposites.get(request.getFloor()).resetUp();
//#endif
//#if DirectedCall3
                break;
//#endif
            case MOVING_DOWN:
//#if DirectedCall
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
//#endif
//#if UndirectedCall
        listFloorComposites.get(request.getFloor()).resetFloorRequest();
//#endif
    }
//#endif
    private void createPanelControlsContent(int maxFloors) {
        JPanel panel_control = new JPanel();
        try {
            panel_control = new JBackgroundPanel(MainWindow.class.getResourceAsStream("/elevator_inside2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        splitPane.setRightComponent(panel_control);
        GridBagLayout gbl_panel_control = new GridBagLayout();
        panel_control.setLayout(gbl_panel_control);
        lblEvent = new JLabel("");
        lblEvent.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblEvent.setForeground(Color.WHITE);
        lblEvent.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lbl = new GridBagConstraints();
        gbc_lbl.gridwidth = 4;
        gbc_lbl.insets = new Insets(0, 0, 185, 0);
        gbc_lbl.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbl.gridx = 0;
        gbc_lbl.gridy = 0;
        panel_control.add(lblEvent, gbc_lbl);
//#if Service
        JToggleButton btnService = new JToggleButton("Service");
//#endif
//#if Service
        btnService.setMinimumSize(new Dimension(80, 30));
//#endif
//#if Service
        btnService.setPreferredSize(new Dimension(80, 30));
//#endif
//#if Service
        btnService.setMaximumSize(new Dimension(80, 30));
//#endif
//#if Service
        GridBagConstraints gbc_btnService = new GridBagConstraints();
//#endif
//#if (( DirectedCall  &&  FloorPermission ) || ( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || ( FIFO  &&  Service )) && ! Sabbath
        gbc_btnService.insets = new Insets(0, 0, 0, 10);
//#endif
//#if (( Service  &&  Sabbath ) || ( FloorPermission  &&  Sabbath )) && ! DirectedCall  && ! FIFO  && ! ShortestPath  && ! UndirectedCall  && ! CallButtons
        gbc_btnService.insets = new Insets(0, 0, 0, 0);
//#endif
//#if (( Service  &&  Sabbath ) || ( FloorPermission  &&  Sabbath )) && ! DirectedCall  && ! FIFO  && ! ShortestPath  && ! UndirectedCall  && ! CallButtons
        gbc_btnService.gridwidth = 4;
//#endif
//#if Service
        gbc_btnService.fill = GridBagConstraints.HORIZONTAL;
//#endif
//#if Service
        gbc_btnService.gridx = 0;
//#endif
//#if Service
        gbc_btnService.gridy = 4;
//#endif
//#if Service
        panel_control.add(btnService, gbc_btnService);
//#endif
//#if Service
        btnService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.toggleService();
                if (sim.isInService()) {
                    setEventLabel("Service-Mode!", Color.ORANGE);
                } else {
                    setEventLabel("", Color.WHITE);
                }
            }
        });
//#endif
//#if CallButtons
        JPanel panel_floors = new JPanel(new GridLayout(0,3));
//#endif
//#if CallButtons
        panel_floors.setBackground(Color.GRAY);
//#endif
//#if CallButtons
        JToggleButton btnFloor;
//#endif
//#if CallButtons
        for (int i = maxFloors; i >= 0; i--) {
            btnFloor = new JToggleButton(String.valueOf(i));
            btnFloor.setActionCommand(String.valueOf(i));
            btnFloor.addActionListener(this);
//#if (( DirectedCall  &&  FloorPermission ) || ( ShortestPath  &&  UndirectedCall  &&  FloorPermission )) && ! FIFO  && ! Sabbath
            btnFloor.setEnabled(sim.isDisabledFloor(i));
//#endif
            panel_floors.add(btnFloor);
            listInnerElevatorControls.add(0, btnFloor);
        }
//#endif
//#if CallButtons
        GridBagConstraints gbc_btnFloor = new GridBagConstraints();
//#endif
//#if CallButtons
        gbc_btnFloor.insets = new Insets(0, 0, 0, 0);
//#endif
//#if CallButtons
        gbc_btnFloor.fill = GridBagConstraints.BOTH;
//#endif
//#if CallButtons
        gbc_btnFloor.gridwidth = 4;
//#endif
//#if CallButtons
        gbc_btnFloor.gridx = 2;
//#endif
//#if CallButtons
        gbc_btnFloor.gridy = 4;
//#endif
//#if CallButtons
        panel_control.add(panel_floors, gbc_btnFloor);
//#endif
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
//#if CallButtons
            JToggleButton btnFloor = listInnerElevatorControls.get(currentFloor);
//#endif
//#if CallButtons
            if(btnFloor.isSelected()) {
                btnFloor.setSelected(false);
                btnFloor.setEnabled(true);
            }
//#endif
            break;
        }
                this.clearPresent();
        this.listFloorComposites.get(currentFloor).showElevatorIsPresent();
    }
//#if CallButtons
    @Override
    public void actionPerformed(ActionEvent e) {
//#if DirectedCall
        sim.floorRequest(new Request(Integer.valueOf(e.getActionCommand()), ElevatorState.FLOORING));
//#endif
//#if UndirectedCall
        sim.floorRequest(new Request(Integer.valueOf(e.getActionCommand())));
//#endif
        listInnerElevatorControls.get(Integer.valueOf(e.getActionCommand())).setEnabled(false);
    }
//#endif
    public void initialize(int maxFloors) {
        if(frmElevatorSample != null) {
            return;
        }
        frmElevatorSample = new JFrame();
        frmElevatorSample.setTitle("Elevator Sample");
        frmElevatorSample.setBounds(100, 50, 900, 650);
        frmElevatorSample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//#if FloorPermission
        FloorChooseDialog permissionDialog = new FloorChooseDialog(maxFloors, Arrays.asList(0), "Choose disabled floors");
//#endif
//#if FloorPermission
        List<Integer> disabledFloors = permissionDialog.getSelectedFloors();
//#endif
//#if FloorPermission
        sim.setDisabledFloors(disabledFloors);
//#endif
//#if FloorPermission
        permissionDialog.dispose();
//#endif
        createBaseStructure();
        createPanelControlsContent(maxFloors);
        addBuilding(maxFloors);
        frmElevatorSample.setVisible(true);
    }
    private void addBuilding(int maxFloors) {
        JPanel panel_building = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel_building.setLayout(layout);
        JScrollPane scrollPane = new JScrollPane(panel_building);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        for (int i = maxFloors; i >= 0; i--) {
//#if DirectedCall
            FloorComposite floor = new FloorComposite(i == 0, i, sim, i == maxFloors);
//#endif
//#if (FIFO || ( ShortestPath  &&  UndirectedCall  &&  FloorPermission ) || ( FloorPermission  &&  Sabbath )) && ! DirectedCall
            FloorComposite floor = new FloorComposite(i == 0, i, sim);
//#endif
//#if Sabbath && ! DirectedCall  && ! FIFO  && ! FloorPermission  && ! ShortestPath  && ! UndirectedCall  && ! CallButtons
            FloorComposite floor = new FloorComposite(i == 0, i);
//#endif
            layout.setConstraints(floor, gbc);
            gbc.gridy += 1;
            panel_building.add(floor);
            listFloorComposites.add(0, floor);
        }
                splitPane.setLeftComponent(scrollPane);
    }
    public void setEventLabel(String text, Color color) {
        if(lblEvent != null) { //1
            lblEvent.setText(text);
            lblEvent.setForeground(color);
        }
    }
    private void createBaseStructure() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        frmElevatorSample.setContentPane(contentPane);
        splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }
//#if DirectedCall
//#endif
}