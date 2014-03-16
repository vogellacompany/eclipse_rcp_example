/**
 */
package extensions.impl;

import extensions.BrowserPart;
import extensions.ExtensionsFactory;
import extensions.ExtensionsPackage;

import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;

import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExtensionsPackageImpl extends EPackageImpl implements ExtensionsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass browserPartEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see extensions.ExtensionsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ExtensionsPackageImpl() {
		super(eNS_URI, ExtensionsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ExtensionsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ExtensionsPackage init() {
		if (isInited) return (ExtensionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExtensionsPackage.eNS_URI);

		// Obtain or create and register package
		ExtensionsPackageImpl theExtensionsPackage = (ExtensionsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ExtensionsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ExtensionsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ApplicationPackageImpl.eINSTANCE.eClass();

		// Create package meta-data objects
		theExtensionsPackage.createPackageContents();

		// Initialize created meta-data
		theExtensionsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theExtensionsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ExtensionsPackage.eNS_URI, theExtensionsPackage);
		return theExtensionsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBrowserPart() {
		return browserPartEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBrowserPart_Uri() {
		return (EAttribute)browserPartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtensionsFactory getExtensionsFactory() {
		return (ExtensionsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		browserPartEClass = createEClass(BROWSER_PART);
		createEAttribute(browserPartEClass, BROWSER_PART__URI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		BasicPackageImpl theBasicPackage = (BasicPackageImpl)EPackage.Registry.INSTANCE.getEPackage(BasicPackageImpl.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		browserPartEClass.getESuperTypes().add(theBasicPackage.getPart());

		// Initialize classes, features, and operations; add parameters
		initEClass(browserPartEClass, BrowserPart.class, "BrowserPart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBrowserPart_Uri(), ecorePackage.getEString(), "uri", null, 0, 1, BrowserPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ExtensionsPackageImpl
