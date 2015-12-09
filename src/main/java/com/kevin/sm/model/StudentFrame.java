package com.kevin.sm.model;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/*
创建一个学生类，通过集合类对1个班的所有学生的信息进行管理，能完成新增、删除和根据学号查找学生信息功能。
创建一个学生信息(学号、姓名、性别、身份证号、学院、专业、班级、个人简介等属性)输入的GUI界面程序,有输入学生信息的控件(性别用Choice控件选择,
个人简介用TextArea控件) 以及保存和关闭按钮，点击保存按钮时，把输入的学生信息存放到“student.txt”文件中去，点击关闭按钮时，退出程序。
*/
public class StudentFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2013050301140L;
	private static String STUDENT_FILE_PATH = "D:\\student.txt";
	private JTable studentsTable = new JTable() {

		private static final long serialVersionUID = 2013050301140L;

		//表格不允许被编辑
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private JScrollPane pane = new JScrollPane();
	private DefaultTableModel model = null;
	private Vector<String> colNames = new Vector<String>();
	Vector<Vector<String>> data = new Vector<Vector<String>>();
	private JButton addButton = new JButton("增加");
	private JButton deleteButton = new JButton("删除");
	private JButton updateButton = new JButton("更新");
	private JButton showButton = new JButton("查看");
	private JButton searchButton = new JButton("查询");
	private JLabel idLabel = new JLabel("学号:");
	private JLabel nameLabel = new JLabel("姓名:");
	private JTextField idField = new JTextField();
	private JTextField nameField = new JTextField();
	public static ArrayList<Student> studentList = new ArrayList<Student>();

	public StudentFrame() {
		super("学生管理系统");
		try {
			init();
			initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化界面
	private void init() {
		colNames.add("学号");
		colNames.add("姓名");
		colNames.add("性别");
		colNames.add("身份证号");
		colNames.add("学院");
		colNames.add("专业");
		colNames.add("班级");
		colNames.add("个人简介");
		model = new DefaultTableModel(null, colNames);
		studentsTable.setModel(model);
		studentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setViewportView(studentsTable);
		pane.setBounds(10, 50, 455, 320);
		showButton.setBounds(480, 50, 100, 22);
		updateButton.setBounds(480, 80, 100, 22);
		addButton.setBounds(480, 110, 100, 22);
		deleteButton.setBounds(480, 140, 100, 22);
		searchButton.setBounds(360, 10, 100, 22);

		idLabel.setBounds(0, 10, 40, 22);
		idLabel.setHorizontalAlignment(JLabel.RIGHT);
		idField.setBounds(45, 10, 120, 22);
		nameLabel.setBounds(160, 10, 60, 22);
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		nameField.setBounds(225, 10, 120, 22);

		showButton.addActionListener(this);
		updateButton.addActionListener(this);
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		searchButton.addActionListener(this);

		Container c = this.getContentPane();
		c.setLayout(null);
		c.add(pane);
		c.add(idLabel);
		c.add(nameLabel);
		c.add(idField);
		c.add(nameField);
		c.add(searchButton);
		c.add(showButton);
		c.add(updateButton);
		c.add(addButton);
		c.add(deleteButton);

		this.setSize(600, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new StudentFrame();
	}

	// Button点击事件
	public void actionPerformed(ActionEvent e) {

		int rowCount = studentsTable.getSelectedRowCount();

		if (e.getSource() == searchButton) {// 查询
			search();
			return;
		}

		if (e.getSource() == addButton) {// 添加
			new StudentListPanel(this, "增加学生信息", true, Tips.insert);
		}

		if (e.getSource() == deleteButton) {// 删除
			if (rowCount < 1) {
				JOptionPane.showMessageDialog(null, "请选择一条信息!");
				return;
			} else if (rowCount > 1) {// 删除多条记录
				for (int i = 0; i < rowCount; i++) {
					// 从最后一个开始删，避免出错
					deleteStudent(studentsTable.getSelectedRows()[rowCount - i - 1]);
				}
			} else {
				deleteStudent(studentsTable.getSelectedRow());
			}
		}

		if (e.getSource() == updateButton) {// 更新
			if (rowCount != 1) {
				JOptionPane.showMessageDialog(null, "请选择一条信息!");
				return;
			}
			Student student = studentList.get(studentsTable.getSelectedRow());
			new StudentListPanel(this, "更新学生信息", true, Tips.update, student);
		}

		if (e.getSource() == showButton) {// 查看
			if (rowCount != 1) {
				JOptionPane.showMessageDialog(null, "请选择一条信息!");
				return;
			}
			Student student = studentList.get(studentsTable.getSelectedRow());
			new StudentListPanel(this, "查看学生信息", true, Tips.show, student);
		}

		showResultList(studentList);
		updateFile();

	}

	// 查询  仅支持按ID查询
	private void search() {
		String id = idField.getText().trim();
		// String name = nameField.getText().trim();

		if (id != null && !"".equals(id)) {
			ArrayList<Student> searchList = new ArrayList<Student>();
			for (int i = 0; i < studentList.size(); i++) {
				if (studentList.get(i).getId().equals(id)) {
					searchList.add(studentList.get(i));
				}
			}
			showResultList(searchList);
		} else {
			showResultList(studentList);
		}

	}

	// 删除记录
	private void deleteStudent(int i) {
		studentList.remove(i);
	}

	// 显示结果集
	private void showResultList(ArrayList<Student> list) {
		Iterator<Student> students = list.iterator();
		data.removeAllElements();// 清空
		while (students.hasNext()) {
			Student student = students.next();
			Vector<String> v = new Vector<String>();
			v.add(student.getId());
			v.add(student.getName());
			v.add(String.valueOf(student.getSex()));
			v.add(student.getIdCard());
			v.add(student.getSchool());
			v.add(student.getProfession());
			v.add(student.getSClass());
			v.add(student.getProfile());
			data.add(v);
		}
		model = new DefaultTableModel(data, colNames);
		studentsTable.setModel(model);
	}

	// 更新配置文件
	private void updateFile() {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(STUDENT_FILE_PATH)));
			Iterator<Student> it = studentList.iterator();
			StringBuffer sb = new StringBuffer();
			String split = "|**|";
			while (it.hasNext()) {
				Student student = it.next();
				sb.append(student.getId()).append(split);
				sb.append(student.getName()).append(split);
				sb.append(student.getSex()).append(split);
				sb.append(student.getIdCard()).append(split);
				sb.append(student.getSchool()).append(split);
				sb.append(student.getProfession()).append(split);
				sb.append(student.getSClass()).append(split);
				sb.append(student.getProfile()).append("\n");
			}
			// System.out.println(sb.toString());
			writer.write(sb.toString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) writer.close();
				writer = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 初始化数据
	private void initData() {
		BufferedReader reader = null;
		try {
			File file = new File(STUDENT_FILE_PATH);
			if (!file.exists()) file.createNewFile();
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "|**|");
				Vector<String> v = new Vector<String>();
				while (st.hasMoreTokens()) {
					v.add(st.nextToken());
				}
				data.add(v);
				//学号、姓名、性别、身份证号、学院、专业、班级、个人简介
				Student student = new Student();
				student.setId(v.get(0));
				student.setName(v.get(1));
				student.setSex(Integer.parseInt(v.get(2)));
				student.setIdCard(v.get(3));
				student.setSchool(v.get(4));
				student.setProfession(v.get(5));
				student.setSClass(v.get(6));
				student.setProfile(v.get(7));
				studentList.add(student);
			}
			model = new DefaultTableModel(data, colNames);
			studentsTable.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) reader.close();
				reader = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

class StudentListPanel extends JDialog implements ActionListener {

	private static final long serialVersionUID = 2013050301140L;

	private JButton saveButton = new JButton("保存");
	private JButton cancelButton = new JButton("取消");
	private JLabel idLabel = new JLabel("学号:");
	private JLabel nameLabel = new JLabel("姓名:");
	private JLabel sexLabel = new JLabel("性别:");
	private JLabel idCardLabel = new JLabel("身份证号:");
	private JLabel schoolLabel = new JLabel("学院:");
	private JLabel professionLabel = new JLabel("专业:");
	private JLabel sClassLabel = new JLabel("班级:");
	private JLabel profileLabel = new JLabel("个人简介:");
	private JTextField idField = new JTextField();
	private JTextField nameField = new JTextField();
	private JTextField idCardField = new JTextField();
	private JTextField schoolField = new JTextField();
	private JTextField sClassField = new JTextField();
	private JTextField professionField = new JTextField();
	private JComboBox<String> sexComboBox = new JComboBox<String>();
	private JTextArea profileArea = new JTextArea();
	private JScrollPane pane = new JScrollPane();
	private Tips tips;// 操作类型
	private Student student;
	private int index;// 学生编号

	// 更新
	public StudentListPanel(JFrame frame, String title, boolean modal, Tips tips, Student student, int index) {
		super(frame, title, modal);
		this.tips = tips;
		this.student = student;
		this.index = index;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查看
	public StudentListPanel(JFrame frame, String title, boolean modal, Tips tips, Student student) {
		super(frame, title, modal);
		this.tips = tips;
		this.student = student;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 增加
	public StudentListPanel(JFrame frame, String title, boolean modal, Tips tips) {
		super(frame, title, modal);
		this.tips = tips;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化界面
	private void init() {

		idLabel.setBounds(0, 10, 60, 22);
		idLabel.setHorizontalAlignment(JLabel.RIGHT);
		idField.setBounds(65, 10, 120, 22);

		nameLabel.setBounds(180, 10, 60, 22);
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		nameField.setBounds(245, 10, 120, 22);

		idCardLabel.setBounds(0, 40, 60, 22);
		idCardLabel.setHorizontalAlignment(JLabel.RIGHT);
		idCardField.setBounds(65, 40, 120, 22);

		sexLabel.setBounds(180, 40, 60, 22);
		sexLabel.setHorizontalAlignment(JLabel.RIGHT);
		sexComboBox.setBounds(245, 40, 120, 22);
		sexComboBox.addItem("====性别====");
		sexComboBox.addItem("男");
		sexComboBox.addItem("女");
		sexComboBox.setEditable(false);

		schoolLabel.setBounds(0, 70, 60, 22);
		schoolLabel.setHorizontalAlignment(JLabel.RIGHT);
		schoolField.setBounds(65, 70, 120, 22);

		professionLabel.setBounds(180, 70, 60, 22);
		professionLabel.setHorizontalAlignment(JLabel.RIGHT);
		professionField.setBounds(245, 70, 120, 22);

		sClassLabel.setBounds(0, 100, 60, 22);
		sClassLabel.setHorizontalAlignment(JLabel.RIGHT);
		sClassField.setBounds(65, 100, 120, 22);

		profileLabel.setBounds(0, 130, 60, 22);
		profileLabel.setHorizontalAlignment(JLabel.RIGHT);
		pane.setBounds(65, 130, 300, 70);
		pane.getViewport().add(profileArea);
		pane.setAutoscrolls(true);

		saveButton.setBounds(95, 250, 100, 22);
		cancelButton.setBounds(205, 250, 100, 22);
		saveButton.addActionListener(this);
		cancelButton.addActionListener(this);

		Container c = this.getContentPane();
		c.setLayout(null);
		c.add(idLabel);
		c.add(idField);
		c.add(nameLabel);
		c.add(nameField);
		c.add(sexLabel);
		c.add(sexComboBox);
		c.add(idCardLabel);
		c.add(idCardField);
		c.add(professionLabel);
		c.add(professionField);
		c.add(schoolLabel);
		c.add(schoolField);

		c.add(sClassLabel);
		c.add(sClassField);
		c.add(profileLabel);
		c.add(pane);

		c.add(saveButton);
		c.add(cancelButton);

		setButton();
		if (student != null) initData();

		this.setSize(400, 350);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	// 设置按钮是否可见
	private void setButton() {
		if (tips.equals(Tips.show)) {
			saveButton.setEnabled(false);
		} else {
			saveButton.setEnabled(true);
		}
	}

	// 初始化数据
	private void initData() {
		idField.setText(student.getId());
		nameField.setText(student.getName());
		idCardField.setText(student.getIdCard());
		schoolField.setText(student.getSchool());
		sClassField.setText(student.getSClass());
		professionField.setText(student.getProfession());
		profileArea.setText(student.getProfile());
		if (student.getSex() == 1) {
			sexComboBox.setSelectedItem("男");
		} else if (student.getSex() == 2) {
			sexComboBox.setSelectedItem("女");
		} else {
			sexComboBox.setSelectedItem("====性别====");
		}

	}

	// 添加记录
	private boolean insertStudent() {
		try {
			Student s = new Student();
			s.setId(idField.getText());
			s.setName(nameField.getText());
			s.setIdCard(idCardField.getText());
			s.setProfession(professionField.getText());
			s.setProfile(profileArea.getText());
			s.setSchool(schoolField.getText());
			s.setSClass(sClassField.getText());
			s.setSex(sexComboBox.getSelectedIndex());
			StudentFrame.studentList.add(s);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 更新记录
	private boolean updateStudent() {
		try {
			student.setId(idField.getText());
			student.setName(nameField.getText());
			student.setIdCard(idCardField.getText());
			student.setProfession(professionField.getText());
			student.setProfile(profileArea.getText());
			student.setSchool(schoolField.getText());
			student.setSClass(sClassField.getText());
			student.setSex(sexComboBox.getSelectedIndex());
			StudentFrame.studentList.set(index, student);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Button点击事件
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {// 确定
			if (tips.equals(Tips.insert)) {// 添加记录
				if (!insertStudent()) {
					JOptionPane.showMessageDialog(null, "添加失败,请重试!");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "添加成功!");
				}
			} else if (tips.equals(Tips.update)) {// 更新记录
				if (!updateStudent()) {
					JOptionPane.showMessageDialog(null, "更新失败,请重试!");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "更新成功!");
				}
			}
		}
		this.dispose();
	}
}

enum Tips {// Student操作类型 更新、查看、添加
	update, show, insert
}

class Student {

	//学号、姓名、性别、身份证号、学院、专业、班级、个人简介
	private String id;
	private String name;
	private int sex;
	private String idCard;
	private String school;
	private String profession;
	private String sClass;
	private String profile;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getSClass() {
		return sClass;
	}

	public void setSClass(String class1) {
		sClass = class1;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

}
